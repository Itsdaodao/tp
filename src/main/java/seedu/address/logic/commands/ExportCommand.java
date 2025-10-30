package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Files;
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
    public static final String MESSAGE_FILE_EXISTS_INFO =
            "File '%1$s' already exists. Exporting to '%2$s' instead.";
    public static final String MESSAGE_FAILURE = "Failed to export contacts: %1$s";
    public static final String MESSAGE_INVALID_FILENAME = "Invalid filename: %1$s\n"
            + "Filename cannot contain: \\ / : * ? \" < > |\n"
            + "Filename cannot be empty or contain only whitespace.";

    private static final String DEFAULT_FILENAME = "contacts";
    private static final String CSV_EXTENSION = ".csv";
    private static final String EXPORT_DIRECTORY = "data";
    private static final int MAX_FILENAME_ATTEMPTS = 1000; // Safety limit

    private static final String INVALID_FILENAME_CHARS = "[\\\\/:*?\"<>|]";

    private final String filename;

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
            // Construct the initial path: data/filename.csv
            String fullFilename = ensureCsvExtension(filename);
            Path initialPath = Paths.get(EXPORT_DIRECTORY, fullFilename);

            // Find an available filename
            Path exportPath = findAvailableFilePath(initialPath);

            // Check if we had to use a different filename
            String feedbackMessage;
            if (!exportPath.equals(initialPath)) {
                feedbackMessage = String.format(MESSAGE_FILE_EXISTS_INFO,
                        initialPath.getFileName(),
                        exportPath.getFileName())
                        + "\n" + String.format(MESSAGE_SUCCESS, exportPath.toAbsolutePath());
            } else {
                feedbackMessage = String.format(MESSAGE_SUCCESS, exportPath.toAbsolutePath());
            }

            // Perform the export
            model.exportAddressBookToCsv(exportPath);

            return new CommandResult(feedbackMessage);
        } catch (IOException e) {
            throw new CommandException(String.format(MESSAGE_FAILURE, e.getMessage()));
        }
    }

    /**
     * Finds an available file path by appending incrementing numbers if the file exists.
     * For example: contacts.csv -> contacts-1.csv -> contacts-2.csv
     *
     * @param initialPath the initial desired path
     * @return an available path that doesn't conflict with existing files
     * @throws IOException if unable to find an available path after MAX_FILENAME_ATTEMPTS
     */
    private Path findAvailableFilePath(Path initialPath) throws IOException {
        // If the initial path doesn't exist, use it
        if (!Files.exists(initialPath)) {
            return initialPath;
        }

        // Extract the base filename without extension
        String fullFilename = initialPath.getFileName().toString();
        String baseFilename = removeExtension(fullFilename);

        // Try appending numbers until we find an available filename
        for (int i = 1; i < MAX_FILENAME_ATTEMPTS; i++) {
            String newFilename = baseFilename + "-" + i + CSV_EXTENSION;
            Path newPath = initialPath.getParent() != null
                    ? initialPath.getParent().resolve(newFilename)
                    : Paths.get(newFilename);

            if (!Files.exists(newPath)) {
                return newPath;
            }
        }

        // If we've exhausted all attempts, throw an exception
        throw new IOException("Unable to find available filename after "
                + MAX_FILENAME_ATTEMPTS + " attempts");
    }

    /**
     * Removes the file extension from a filename.
     * For example: "contacts.csv" -> "contacts"
     *
     * @param filename the filename with extension
     * @return the filename without extension
     */
    private String removeExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return filename.substring(0, lastDotIndex);
        }
        return filename;
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
