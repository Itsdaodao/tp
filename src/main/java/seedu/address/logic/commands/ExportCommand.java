package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Exports all contacts in the address book to a CSV file.
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Exports all contacts to a CSV file.\n"
            + "Parameters: FILEPATH (optional, defaults to data/contacts.csv)\n"
            + "Example: " + COMMAND_WORD + " mycontacts.csv";

    public static final String MESSAGE_SUCCESS = "Contacts exported successfully to: %1$s";
    public static final String MESSAGE_FAILURE = "Failed to export contacts: %1$s";

    private static final String DEFAULT_EXPORT_PATH = "data/contacts.csv";

    private final String filePath;

    /**
     * Creates an ExportCommand with the default file path.
     */
    public ExportCommand() {
        this.filePath = DEFAULT_EXPORT_PATH;
    }

    /**
     * Creates an ExportCommand to export to the specified file path.
     */
    public ExportCommand(String filePath) {
        this.filePath = filePath != null && !filePath.trim().isEmpty()
                ? filePath.trim()
                : DEFAULT_EXPORT_PATH;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        try {
            Path exportPath = Paths.get(filePath);
            model.exportAddressBookToCsv(exportPath);
            return new CommandResult(String.format(MESSAGE_SUCCESS, exportPath.toAbsolutePath()));
        } catch (IOException e) {
            throw new CommandException(String.format(MESSAGE_FAILURE, e.getMessage()));
        }
    }

    @Override
    public boolean requiresWrite() {
        return false; // Export doesn't modify the address book
    }

    /**
     * Registers this command's help information with the CommandRegistry.
     */
    public static void registerHelp() {
        CommandRegistry.register(
                COMMAND_WORD,
                "Exports all contacts to a CSV file",
                "Example: " + COMMAND_WORD + " mycontacts.csv\n"
                        + "            Example: export",
                "Usage: " + MESSAGE_USAGE
        );
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ExportCommand)) {
            return false;
        }

        ExportCommand otherCommand = (ExportCommand) other;
        return filePath.equals(otherCommand.filePath);
    }
}
