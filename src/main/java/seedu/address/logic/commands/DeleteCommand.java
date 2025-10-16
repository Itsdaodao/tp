package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";

    public static final String MESSAGE_DELETE_PERSON_CONFIRM = "Confirm deletion [y/n] of:\n%1$s?";

    private final Index targetIndex;

    public DeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getSortedAndFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToDelete = lastShownList.get(targetIndex.getZeroBased());
        return new ConfirmationPendingResult(
                String.format(MESSAGE_DELETE_PERSON_CONFIRM, Messages.format(personToDelete)),
                false,
                false, () -> {
                    model.deletePerson(personToDelete);
                    return new CommandResult(
                            String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete))
                    );
                }
        );
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return targetIndex.equals(otherDeleteCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }

    public static void registerHelp() {
        CommandRegistry.register(
                COMMAND_WORD,
                "Deletes a contact by index number",
                "Example: delete 1",
                "Usage: delete INDEX\n\n"
                        + "Deletes the contact at the specified index from the address book.\n\n"
                        + "Parameters:\n"
                        + "  INDEX - The index number shown in the displayed contact list (required)\n\n"
                        + "Notes:\n"
                        + "  - The index must be a positive integer (1, 2, 3, ...)\n"
                        + "  - The index refers to the contact's position in the currently displayed list"
        );
    }
}
