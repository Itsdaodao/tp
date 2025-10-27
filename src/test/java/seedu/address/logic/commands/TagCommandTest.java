package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;


public class TagCommandTest {
    private Model model;
    private final Tag targetTag = new Tag("friends");
    private final Tag renamedTag = new Tag("buddies");

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_renameMultiplePersonsUpdated_success() {
        // Give multiple people the same target tag
        Person first = model.getSortedAndFilteredPersonList().get(0);
        Person second = model.getSortedAndFilteredPersonList().get(1);
        Person third = model.getSortedAndFilteredPersonList().get(2);

        Person updatedFirst = new PersonBuilder(first).withTags("friends").build();
        Person updatedSecond = new PersonBuilder(second).withTags("friends").build();
        Person updatedThird = new PersonBuilder(third).withTags("friends").build();

        model.setPerson(first, updatedFirst);
        model.setPerson(second, updatedSecond);
        model.setPerson(third, updatedThird);
        Set<Tag> target = Set.of(targetTag);
        Set<Tag> renamed = Set.of(renamedTag);
        TagCommand command = new TagCommand(target, renamed, TagOperation.RENAME);

        String expectedMessage = String.format(TagCommand.MESSAGE_RENAMED_SUCCESS,
                targetTag, renamedTag, 3);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(updatedFirst, new PersonBuilder(updatedFirst).withTags("buddies").build());
        expectedModel.setPerson(updatedSecond, new PersonBuilder(updatedSecond).withTags("buddies").build());
        expectedModel.setPerson(updatedThird, new PersonBuilder(third).withTags("buddies").build());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteTagForMultiplePersons_success() {
        // Give multiple people the target tag
        Person first = model.getSortedAndFilteredPersonList().get(0);
        Person second = model.getSortedAndFilteredPersonList().get(1);

        Person updatedFirst = new PersonBuilder(first).withTags("stranger", "colleagues").build();
        Person updatedSecond = new PersonBuilder(second).withTags("stranger").build();

        model.setPerson(first, updatedFirst);
        model.setPerson(second, updatedSecond);

        Set<Tag> target = Set.of(new Tag("stranger"));
        TagCommand command = new TagCommand(target, Collections.emptySet(), TagOperation.DELETE);

        String expectedMessage = String.format(TagCommand.MESSAGE_DELETE_SUCCESS, target);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(updatedFirst, new PersonBuilder(first).withTags("colleagues").build());
        expectedModel.setPerson(updatedSecond, new PersonBuilder(second).withTags().build()); // no tags

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteMultipleTagsForMultiplePersons_success() {
        // Give multiple people the target tag
        Person first = model.getSortedAndFilteredPersonList().get(0);
        Person second = model.getSortedAndFilteredPersonList().get(1);
        Person third = model.getSortedAndFilteredPersonList().get(2);

        Person updatedFirst = new PersonBuilder(first).withTags("enemy", "colleagues").build();
        Person updatedSecond = new PersonBuilder(second).withTags("stranger").build();
        Person updatedThird = new PersonBuilder(third).withTags("stranger", "enemy", "friend").build();

        model.setPerson(first, updatedFirst);
        model.setPerson(second, updatedSecond);
        model.setPerson(third, updatedThird);

        Set<Tag> target = Set.of(new Tag("stranger"), new Tag("enemy"));
        TagCommand command = new TagCommand(target, Collections.emptySet(), TagOperation.DELETE);

        String expectedMessage = String.format(TagCommand.MESSAGE_DELETE_SUCCESS, target);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(updatedFirst, new PersonBuilder(first).withTags("colleagues").build());
        expectedModel.setPerson(updatedSecond, new PersonBuilder(second).withTags().build()); // no tags
        expectedModel.setPerson(updatedThird, new PersonBuilder(third).withTags("friend").build());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_renameNoPersonsWithTag_throwsCommandException() {
        Set<Tag> target = Set.of(new Tag("missingTag"));
        Set<Tag> renamed = Set.of(new Tag("whatever"));
        TagCommand command = new TagCommand(target, renamed, TagOperation.RENAME);

        assertCommandFailure(command, model,
                String.format(TagCommand.MESSAGE_TAG_FAILURE, target));
    }

    @Test
    public void execute_deleteNoPersonsWithTag_throwsCommandException() {
        Set<Tag> target = Set.of(new Tag("missingTag"));
        TagCommand command = new TagCommand(target, Collections.emptySet(), TagOperation.DELETE);

        assertCommandFailure(command, model,
                String.format(TagCommand.MESSAGE_TAG_FAILURE, target));
    }

    @Test
    public void equals() {
        Set<Tag> target1 = Set.of(new Tag("t1"));
        Set<Tag> renamed1 = Set.of(new Tag("r1"));
        Set<Tag> target2 = Set.of(new Tag("t2"));
        Set<Tag> renamed2 = Set.of(new Tag("r2"));

        TagCommand command1 = new TagCommand(target1, renamed1, TagOperation.RENAME);
        TagCommand command2 = new TagCommand(target1, renamed1, TagOperation.RENAME);
        TagCommand command3 = new TagCommand(target2, renamed1, TagOperation.RENAME);
        TagCommand command4 = new TagCommand(target1, renamed2, TagOperation.RENAME);
        TagCommand command5 = new TagCommand(target1, renamed1, TagOperation.DELETE);

        // Same object
        assertEquals(command1, command1);

        // Equal objects
        assertEquals(command1, command2);

        // Different targetTags
        assertNotEquals(command1, command3);

        // Different renamedTags
        assertNotEquals(command1, command4);

        // Different operation
        assertNotEquals(command1, command5);

        // Null and different type
        assertNotEquals(command1, null);
        assertNotEquals(command1, new Object());
    }
}
