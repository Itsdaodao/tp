package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n"
            + "Example: " + COMMAND_WORD;

    public static final String SHOWING_HELP_MESSAGE = "Available commands:\n\n"
            + "add         Adds a contact to the address book\n"
            + "            Example: add n/John Doe p/98765432 e/johnd@example.com a/123 Main St\n"
            + "clear       Clears all contacts from the address book\n"
            + "delete      Deletes a contact from the address book\n"
            + "edit        Edits an existing contact\n"
            + "exit        Exits the program\n"
            + "find        Finds contacts whose names contain any of the given keywords\n"
            + "            Example: find alice bob\n"
            + "help        Shows program usage instructions\n"
            + "list        Lists out all contacts\n"
            + "            Example: edit 1 p/91234567 e/johndoe@example.com\n"
            + "            Example: delete 1\n";

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(SHOWING_HELP_MESSAGE);
    }

    @Override
    public boolean requiresWrite() {
        return false;
    }
}
