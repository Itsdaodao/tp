package seedu.address.logic.parser;

import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new HelpCommand object.
 */
public class HelpCommandParser implements Parser<HelpCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the HelpCommand
     * and returns a HelpCommand object for execution.
     * Returns a general help command if no arguments are provided,
     * or a specific help command for the given command name.
     *
     * @param args The user input arguments after the command word
     * @return A {@code HelpCommand} object for displaying help information
     * @throws ParseException if the user input does not conform to the expected format
     */
    public HelpCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        // If no arguments, return general help
        if (trimmedArgs.isEmpty()) {
            return new HelpCommand();
        }

        // Validate that it's a single word (no spaces allowed)
        if (trimmedArgs.contains(" ")) {
            throw new ParseException(
                    "Invalid command format. Please specify only one command name.\n"
                            + HelpCommand.MESSAGE_USAGE);
        }

        // Remove leading dash if present (support both "help add" and "help -add")
        String commandName = trimmedArgs.startsWith("-")
                ? trimmedArgs.substring(1)
                : trimmedArgs;

        // Validate that command name is not empty after removing dash
        if (commandName.isEmpty()) {
            throw new ParseException(
                    "Invalid command format. Please specify a command name after the dash.\n"
                            + HelpCommand.MESSAGE_USAGE);
        }

        // Convert to lowercase for case-insensitive matching
        commandName = commandName.toLowerCase();

        // Return help command for specific command
        return new HelpCommand(commandName);
    }
}
