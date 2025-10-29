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
            + "Parameters: FILENAME (optional, defaults to contacts)\n"
            + "The .csv extension will be added automatically.\n"
            + "Files are saved in the data/ directory.\n"
            + "Example: " + COMMAND_WORD + " mycontacts\n"
            + "         " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Contacts exported successfully to: %1$s";
    public static final String MESSAGE_FAILURE = "Failed to export contacts: %1$s";
    public static final String MESSAGE_INVALID_FILENAME = "Invalid filename: %1$s\n"
            + "Filename cannot contain: \\ / : * ? \" < > |\n"
            + "Filename cannot be empty or contain only whitespace.";

    private static final String DEFAULT_FILENAME = "contacts";
    private static final String CSV_EXTENSION = ".csv";
    private static final String EXPORT_DIRECTORY = "data";

    private static final String INVALID_FILENAME_CHARS = "[\\\\/:*?\"<>|]";

    private final String filename;

    /**
     * Creates an ExportCommand with the default file path.
     */
    /**
     * Creates an ExportCommand with the default filename.
     */
    public ExportCommand() {
        this.filename = DEFAULT_FILENAME;
    }

    /**
     * Creates an ExportCommand to export to the specified filename.
     * The filename will be sanitized and the .csv extension will be added.
     *
     * @param filename the base filename (without extension)
     */
    public ExportCommand(String filename) {
        this.filename = filename != null && !filename.trim().isEmpty()
                ? sanitizeFilename(filename.trim())
                : DEFAULT_FILENAME;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        try {
            // Construct the full path: data/filename.csv
            String fullFilename = ensureCsvExtension(filename);
            Path exportPath = Paths.get(EXPORT_DIRECTORY, fullFilename);

            model.exportAddressBookToCsv(exportPath);
            return new CommandResult(String.format(MESSAGE_SUCCESS, exportPath.toAbsolutePath()));
        } catch (IOException e) {
            throw new CommandException(String.format(MESSAGE_FAILURE, e.getMessage()));
        }
    }

    /**
     * Sanitizes the filename by removing invalid characters.
     * If the user includes an extension (e.g., "file.pdf"), it becomes part of the base name.
     *
     * @param filename the filename to sanitize
     * @return the sanitized filename
     */
    private static String sanitizeFilename(String filename) {
        // Remove any invalid characters
        String sanitized = filename.replaceAll(INVALID_FILENAME_CHARS, "");

        // Remove leading/trailing whitespace and dots
        sanitized = sanitized.trim().replaceAll("^\\.+|\\.+$", "");

        // If empty after sanitization, use default
        if (sanitized.isEmpty()) {
            return DEFAULT_FILENAME;
        }

        return sanitized;
    }

    /**
     * Ensures the filename ends with .csv extension.
     * If the user provided "file.pdf", it becomes "file.pdf.csv"
     * If the user provided "file.csv", it stays "file.csv"
     * If the user provided "file", it becomes "file.csv"
     *
     * @param filename the base filename
     * @return filename with .csv extension
     */
    private static String ensureCsvExtension(String filename) {
        if (filename.toLowerCase().endsWith(CSV_EXTENSION)) {
            return filename;
        }
        return filename + CSV_EXTENSION;
    }

    /**
     * Validates that a filename doesn't contain invalid characters.
     *
     * @param filename the filename to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidFilename(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            return false;
        }

        String trimmed = filename.trim();

        // Check for invalid characters
        if (trimmed.matches(".*" + INVALID_FILENAME_CHARS + ".*")) {
            return false;
        }

        // Check if it's only dots
        if (trimmed.matches("^\\.+$")) {
            return false;
        }

        return true;
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
                "Example: " + COMMAND_WORD + " mycontacts\n"
                        + "         " + COMMAND_WORD,
                MESSAGE_USAGE
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
        return filename.equals(otherCommand.filename);
    }

    @Override
    public int hashCode() {
        return filename.hashCode();
    }

    @Override
    public String toString() {
        return "ExportCommand{filename=" + filename + "}";
    }
}
