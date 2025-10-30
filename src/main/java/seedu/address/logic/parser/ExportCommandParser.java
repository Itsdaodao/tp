package seedu.address.logic.parser;

import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ExportCommand object
 */
public class ExportCommandParser implements Parser<ExportCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ExportCommand
     * and returns an ExportCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public ExportCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        // Empty args = use default filename
        if (trimmedArgs.isEmpty()) {
            return new ExportCommand();
        }

        // Validate filename (check for completely invalid input)
        // Note: ExportCommand will sanitize the filename, but we want to warn users
        // about obviously invalid input
        if (!ExportCommand.isValidFilename(trimmedArgs)) {
            throw new ParseException(
                    String.format(ExportCommand.MESSAGE_INVALID_FILENAME, trimmedArgs)
            );
        }

        return new ExportCommand(trimmedArgs);
    }
}
