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


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setAddressBook(new AddressBook());
        return new CommandResult(MESSAGE_SUCCESS);
    }

    /**
     * @inheritDoc
     * @return <code>true</code> as ClearCommand modifies the address book
     */
    @Override
    public boolean requiresWrite() {
        return true;
    }

    public static void registerHelp() {
        CommandRegistry.register(
                COMMAND_WORD,
                "Clears all contacts from the address book",
                null
        );
    }
}
