package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.FLAG_DELETE_TAG;
import static seedu.address.logic.parser.CliSyntax.FLAG_RENAME_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RENAMED_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TARGET_TAG;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Edits the Tag details of single or multiple contacts in the address book.
 */
public class TagCommand extends Command {

    public static final String COMMAND_WORD = "tag";

    public static final String MESSAGE_DESCRIPTION =
            "Edits the tags details for all persons identified in the displayed person list.";

    public static final String MESSAGE_FLAGS = "Flags: ["
            + FLAG_RENAME_TAG + " | "
            + FLAG_DELETE_TAG + "]";

    public static final String MESSAGE_PARAMETERS = "Parameters: "
            + PREFIX_TARGET_TAG + "(target) TAG "
            + "[" + PREFIX_RENAMED_TAG + "(renamed) TAG]";

    public static final String MESSAGE_EXAMPLE = "Example Rename Tag: "
            + COMMAND_WORD + " " + FLAG_RENAME_TAG + " "
            + PREFIX_TARGET_TAG + "CS1101" + " " + PREFIX_RENAMED_TAG + "CS2100"
            + "\n"
            + "Example Delete Tag: "
            + COMMAND_WORD + " " + FLAG_DELETE_TAG + " " + PREFIX_TARGET_TAG + "CS1101";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": "
            + MESSAGE_DESCRIPTION + "\n"
            + MESSAGE_FLAGS + "\n"
            + MESSAGE_PARAMETERS + "\n"
            + MESSAGE_EXAMPLE;

    public static final String MESSAGE_RENAMED_SUCCESS = "Renamed tag %s to %s for %d person(s).";
    public static final String MESSAGE_TAG_FAILURE = "No persons found with tag: %s";
    public static final String MESSAGE_MULTIPLE_FLAGS_INVALID = "Invalid command! multiple flags found\n" + MESSAGE_USAGE;
    public static final String MESSAGE_NO_FLAG_PROVIDED = "No/Invalid flag provided\n" + MESSAGE_USAGE;
    public static final String MESSAGE_NO_TARGET_TAG_PROVIDED = "No target tags provided\n" + MESSAGE_USAGE;
    public static final String MESSAGE_RENAMED_EXACTLY_ONE = "Must have exactly one target tag and one renamed tag";
    public static final String MESSAGE_RENAMED_TAG_FOUND_UNSUITABLE =
            "Operation not suitable with renamed tag, try command without the renamed tag";

    public static final String MESSAGE_DELETE_SUCCESS = "Deleted tags: %s";

    public static final String MESSAGE_INVALID_OPERATION = "Invalid tag command operation";

    private final TagOperation tagOperation;

    private final Set<Tag> targetTags;
    private final Set<Tag> renamedTags;

    /**
     * Creates a TagCommand to delete/rename a specified tag from multiple people
     *
     * @param targetTag     set of target tags to delete/rename
     * @param renamedTags   name of tags to be renamed to
     * @param tagOperation   operation mode of the tag command
     */
    public TagCommand(Set<Tag> targetTag, Set<Tag> renamedTags, TagOperation tagOperation) {
        this.targetTags = targetTag;
        this.renamedTags = renamedTags;
        this.tagOperation = tagOperation;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownList = model.getSortedAndFilteredPersonList();

        switch (tagOperation) {
        case RENAME:
            return executeRenameTags(model, lastShownList);
        case DELETE:
            return executeDeleteTags(model, lastShownList);
        default:
            throw new CommandException(MESSAGE_INVALID_OPERATION);
        }
    }

    /**
     * Executes the TagCommand given the rename flag {@code -r} and returns the result message.
     *
     * @param model {@code Model} which the command should operate on.
     * @param lastShownList {@code lastShownList} which the command should iterate on.
     * @return feedback message of the rename tag result for display
     * @throws CommandException
     */
    private CommandResult executeRenameTags(Model model, List<Person> lastShownList) throws CommandException {
        assert tagOperation == TagOperation.RENAME;
        assert targetTags.size() == 1;

        Tag targetTag = targetTags.iterator().next();
        Tag renamedTag = renamedTags.iterator().next();
        int updatedCount = 0;

        for (Person personToEdit : lastShownList) {
            // Skip people who don't have the target tag
            if (!personToEdit.getTags().contains(targetTag)) {
                continue;
            }
            Person updatedPerson = createPerson(personToEdit);
            model.setPerson(personToEdit, updatedPerson);
            updatedCount++;
        }

        if (updatedCount == 0) {
            throw new CommandException(String.format(MESSAGE_TAG_FAILURE, targetTags));
        }

        return new CommandResult(String.format(MESSAGE_RENAMED_SUCCESS, targetTag, renamedTag, updatedCount));
    }

    /**
     * Executes the TagCommand to delete tag given the delete flag {@code -d} and returns the result message.
     *
     * @param model {@code Model} which the command should operate on.
     * @param lastShownList {@code lastShownList} which the command should iterate on.
     * @return feedback message of the deleted tags result for display
     * @throws CommandException
     */
    private CommandResult executeDeleteTags(Model model, List<Person> lastShownList) throws CommandException {
        assert tagOperation == TagOperation.DELETE;
        assert !targetTags.isEmpty();

        boolean noTagsFoundAndDeleted = true;

        for (Person personToEdit : lastShownList) {
            Person personWithTagsRemoved = removeTagsFromPerson(personToEdit);

            if (personWithTagsRemoved.equals(personToEdit)) {
                continue; // No change to Person, continue to next Person
            }

            model.setPerson(personToEdit, personWithTagsRemoved);
            noTagsFoundAndDeleted = false;
        }

        if (noTagsFoundAndDeleted) {
            throw new CommandException(String.format(MESSAGE_TAG_FAILURE, targetTags));
        }

        return new CommandResult(String.format(MESSAGE_DELETE_SUCCESS, targetTags));
    }

    private Person createPerson(Person personToEdit) {
        Set<Tag> updatedTagSet = getUpdatedTagSet(personToEdit);

        return new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getTelegram(),
                personToEdit.getGithub(),
                personToEdit.getPreferredMode(),
                updatedTagSet,
                personToEdit.isPinned(),
                personToEdit.getPinnedAt().orElse(null)
        );
    }

    private Set<Tag> getUpdatedTagSet(Person person) {
        Set<Tag> baseTags = new HashSet<>(person.getTags());

        assert !baseTags.isEmpty() : "baseTags should contain at least the targetTag";
        assert renamedTags.size() == 1 : "renamedTag should contain exactly one tag to be renamed";

        baseTags.removeAll(targetTags);
        baseTags.addAll(renamedTags);

        return baseTags;
    }

    /**
     * Returns a person with all target tags removed if target tag found
     */
    private Person removeTagsFromPerson(Person personToEdit) {
        Set<Tag> tags = new HashSet<>(personToEdit.getTags());

        tags.removeAll(targetTags);

        return new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getTelegram(),
                personToEdit.getGithub(),
                personToEdit.getPreferredMode(),
                tags,
                personToEdit.isPinned(),
                personToEdit.getPinnedAt().orElse(null)
        );
    }

    /**
     * @return <code>true</code> as TagCommand modifies the address book
     * @inheritDoc
     */
    @Override
    public boolean requiresWrite() {
        return true;
    }

    /**
     * Registers the launch command with the command registry, providing detailed help information
     * including usage syntax, parameters, and examples for user reference.
     * This method is called during application initialization to make the command
     * available in the help system.
     */
    public static void registerHelp() {
        CommandRegistry.register(
                COMMAND_WORD,
                MESSAGE_DESCRIPTION,
                MESSAGE_EXAMPLE,
                MESSAGE_PARAMETERS
        );
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TagCommand)) {
            return false;
        }
        TagCommand otherCommand = (TagCommand) other;
        return targetTags.equals(otherCommand.targetTags)
                && renamedTags.equals(otherCommand.renamedTags)
                && tagOperation == otherCommand.tagOperation;
    }
}
