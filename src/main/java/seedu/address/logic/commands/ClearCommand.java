package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";
    public static final String MESSAGE_CLEAR_CONFIRM =
            String.format("Are you sure you want to clear the address book?"
                            + " This action cannot be undone.\nConfirm with %s", ConfirmCommand.USER_INPUT_OPTIONS);


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        return new ConfirmationPendingResult(
                String.format(MESSAGE_CLEAR_CONFIRM),
                false, false, () -> {
                    model.setAddressBook(new AddressBook());
                    return new CommandResult(MESSAGE_SUCCESS);
                }
        );
    }

    /**
     * @inheritDoc
     * @return <code>true</code> as ClearCommand modifies the address book
     */
    @Override
    public boolean requiresWrite() {
        return true;
    }

    /**
     * Registers the clear command with the command registry, providing detailed help information
     * including usage syntax, parameters, and examples for user reference.
     * This method is called during application initialization to make the command
     * available in the help system.
     */
    public static void registerHelp() {
        CommandRegistry.register(
                COMMAND_WORD,
                "Clears all contacts from the address book",
                "Example: clear",
                "Usage: clear\n\n"
                        + "Removes all contacts from the address book.\n\n"
                        + "Notes:\n"
                        + "  - This action cannot be undone\n"
                        + "  - All contact data will be permanently deleted"
        );
    }
}
