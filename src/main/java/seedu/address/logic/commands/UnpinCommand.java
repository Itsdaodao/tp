package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Updates the status of existing person as unpinned
 */
public class UnpinCommand extends Command {

    public static final String COMMAND_WORD = "unpin";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Unpin a pinned person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_UNPIN_PERSON_SUCCESS = "Unpinned: %1$s";

    public static final String MESSAGE_PERSON_NOT_PINNED = "Person is currently not pinned.";

    private final Index targetIndex;

    public UnpinCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getSortedAndFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(
                    Messages.getMessageInvalidPersonDisplayedIndex(targetIndex.getOneBased(), lastShownList.size()));
        }

        Person personToUnpin = lastShownList.get(targetIndex.getZeroBased());
        Person unpinnedPerson = personToUnpin.unpin();

        assert !unpinnedPerson.isPinned() : "Person is still pinned after unpinning.";
        assert unpinnedPerson.getPinnedAt().isEmpty() : "Pinned timestamp still exists after unpinning.";

        if (personToUnpin.equals(unpinnedPerson)) {
            // No changes, person is not yet pinned
            throw new CommandException(MESSAGE_PERSON_NOT_PINNED);
        }

        model.setPerson(personToUnpin, unpinnedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(
                String.format(MESSAGE_UNPIN_PERSON_SUCCESS, Messages.format(unpinnedPerson))
        );
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UnpinCommand)) {
            return false;
        }

        UnpinCommand otherUnpinCommand = (UnpinCommand) other;
        return targetIndex.equals(otherUnpinCommand.targetIndex);
    }

    /**
     * @inheritDoc
     * @return <code>true</code> as UnpinCommand modifies the address book
     */
    @Override
    public boolean requiresWrite() {
        return true;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }

    /**
     * Registers the unpin command with the command registry, providing detailed help information
     * including usage syntax, parameters, and examples for user reference.
     * This method is called during application initialization to make the command
     * available in the help system.
     */
    public static void registerHelp() {
        CommandRegistry.register(
                COMMAND_WORD,
                "Unpin a pinned contact by index number",
                "Example: unpin 1",
                "Usage: unpin INDEX\n\n"
                        + "Unpin the contact at the specified index from the address book.\n\n"
                        + "Parameters:\n"
                        + "  INDEX - The index number shown in the displayed contact list (required)\n\n"
                        + "Notes:\n"
                        + "  - The index must be a positive integer (1, 2, 3, ...)\n"
                        + "  - The index refers to the contact's position in the currently displayed list"
        );
    }
}
