package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code UnpinCommand}.
 */
public class UnpinCommandTest {

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        // After sorting, the first two persons (Alice and Daniel) are pinned.
        Person personToUnpin = model.getSortedAndFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        UnpinCommand unpinCommand = new UnpinCommand(INDEX_FIRST_PERSON);

        Person unpinnedPerson = personToUnpin.unpin();
        String expectedMessage = String.format(
                UnpinCommand.MESSAGE_UNPIN_PERSON_SUCCESS, Messages.format(unpinnedPerson));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToUnpin, unpinnedPerson);

        assertCommandSuccess(unpinCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Index outOfBoundIndex = Index.fromOneBased(model.getSortedAndFilteredPersonList().size() + 1);
        UnpinCommand unpinCommand = new UnpinCommand(outOfBoundIndex);

        assertCommandFailure(unpinCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        showPersonAtIndex(model, INDEX_FIRST_PERSON); // Show a pinned person (Alice)

        Person personToUnpin = model.getSortedAndFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        UnpinCommand unpinCommand = new UnpinCommand(INDEX_FIRST_PERSON);

        Person unpinnedPerson = personToUnpin.unpin();
        String expectedMessage = String.format(
                UnpinCommand.MESSAGE_UNPIN_PERSON_SUCCESS, Messages.format(unpinnedPerson));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToUnpin, unpinnedPerson);
        // The list should show all persons after a successful pin command.

        assertCommandSuccess(unpinCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        UnpinCommand unpinCommand = new UnpinCommand(outOfBoundIndex);

        assertCommandFailure(unpinCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_personNotPinned_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        // The person at the third index of the sorted list is not pinned.
        UnpinCommand unpinCommand = new UnpinCommand(INDEX_THIRD_PERSON);
        assertCommandFailure(unpinCommand, model, UnpinCommand.MESSAGE_PERSON_NOT_PINNED);
    }

    @Test
    public void equals() {
        UnpinCommand unpinFirstCommand = new UnpinCommand(INDEX_FIRST_PERSON);
        UnpinCommand unpinSecondCommand = new UnpinCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(unpinFirstCommand.equals(unpinFirstCommand));

        // same values -> returns true
        UnpinCommand unpinFirstCommandCopy = new UnpinCommand(INDEX_FIRST_PERSON);
        assertTrue(unpinFirstCommand.equals(unpinFirstCommandCopy));

        // different types -> returns false
        assertFalse(unpinFirstCommand.equals(1));

        // null -> returns false
        assertFalse(unpinFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(unpinFirstCommand.equals(unpinSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        UnpinCommand unpinCommand = new UnpinCommand(targetIndex);
        String expected = UnpinCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, unpinCommand.toString());
    }
}
