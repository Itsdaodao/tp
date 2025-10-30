package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ExportCommandTest {

    @TempDir
    public Path temporaryFolder;

    private Model model;
    private Path testFilePath;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), new CommandHistory());
        testFilePath = temporaryFolder.resolve("testexport.csv");
    }

    @AfterEach
    public void tearDown() throws IOException {
        // Clean up test files in data directory
        Path dataDir = Path.of("data");
        if (Files.exists(dataDir)) {
            Files.walk(dataDir)
                    .sorted((a, b) -> -a.compareTo(b))
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            // Ignore cleanup errors
                        }
                    });
            Files.deleteIfExists(dataDir);
        }
    }

    @Test
    public void isValidFilename_validFilenames_returnsTrue() {
        assertTrue(ExportCommand.isValidFilename("contacts"));
        assertTrue(ExportCommand.isValidFilename("my_contacts"));
        assertTrue(ExportCommand.isValidFilename("contacts-2024"));
        assertTrue(ExportCommand.isValidFilename("contacts.backup"));
        assertTrue(ExportCommand.isValidFilename("123"));
        assertTrue(ExportCommand.isValidFilename("a"));
        assertTrue(ExportCommand.isValidFilename("my file"));
        assertTrue(ExportCommand.isValidFilename("file.with.dots"));
    }

    @Test
    public void isValidFilename_invalidFilenames_returnsFalse() {
        // Null or empty
        assertFalse(ExportCommand.isValidFilename(null));
        assertFalse(ExportCommand.isValidFilename(""));
        assertFalse(ExportCommand.isValidFilename("   "));

        // Invalid characters
        assertFalse(ExportCommand.isValidFilename("file/name"));
        assertFalse(ExportCommand.isValidFilename("file\\name"));
        assertFalse(ExportCommand.isValidFilename("file:name"));
        assertFalse(ExportCommand.isValidFilename("file*name"));
        assertFalse(ExportCommand.isValidFilename("file?name"));
        assertFalse(ExportCommand.isValidFilename("file\"name"));
        assertFalse(ExportCommand.isValidFilename("file<name"));
        assertFalse(ExportCommand.isValidFilename("file>name"));
        assertFalse(ExportCommand.isValidFilename("file|name"));

        // Only dots
        assertFalse(ExportCommand.isValidFilename("."));
        assertFalse(ExportCommand.isValidFilename(".."));
        assertFalse(ExportCommand.isValidFilename("..."));
    }

    @Test
    public void execute_filenameWithoutExtension_addsCsvExtension() throws CommandException, IOException {
        ExportCommand command = new ExportCommand("myfile");
        command.execute(model);

        Path expectedPath = Path.of("data/myfile.csv");
        assertTrue(Files.exists(expectedPath), "File should be created with .csv extension");
    }

    @Test
    public void constructor_nullFilename_usesDefault() {
        ExportCommand command = new ExportCommand(null);
        ExportCommand defaultCommand = new ExportCommand();
        assertEquals(defaultCommand, command);
    }

    @Test
    public void constructor_emptyFilename_usesDefault() {
        ExportCommand command = new ExportCommand("");
        ExportCommand defaultCommand = new ExportCommand();
        assertEquals(defaultCommand, command);
    }

    @Test
    public void constructor_whitespaceFilename_usesDefault() {
        ExportCommand command = new ExportCommand("   ");
        ExportCommand defaultCommand = new ExportCommand();
        assertEquals(defaultCommand, command);
    }

    @Test
    public void execute_filenameWithCsvExtension_keepsExtension() throws CommandException, IOException {
        ExportCommand command = new ExportCommand("myfile.csv");
        command.execute(model);

        Path expectedPath = Path.of("data/myfile.csv");
        assertTrue(Files.exists(expectedPath), "File should be created as myfile.csv");
        assertFalse(Files.exists(Path.of("data/myfile.csv.csv")),
                "Should not create double extension");
    }

    @Test
    public void execute_filenameWithOtherExtension_appendsCsvExtension() throws CommandException, IOException {
        ExportCommand command = new ExportCommand("myfile.pdf");
        command.execute(model);

        Path expectedPath = Path.of("data/myfile.pdf.csv");
        assertTrue(Files.exists(expectedPath),
                "File should be created as myfile.pdf.csv (original extension becomes part of name)");
    }

    @Test
    public void execute_filenameWithMultipleExtensions_appendsCsvExtension() throws CommandException, IOException {
        ExportCommand command = new ExportCommand("myfile.backup.old");
        command.execute(model);

        Path expectedPath = Path.of("data/myfile.backup.old.csv");
        assertTrue(Files.exists(expectedPath), "File should preserve all parts of filename");
    }

    @Test
    public void execute_filenameWithInvalidChars_removesInvalidChars() throws CommandException, IOException {
        ExportCommand command = new ExportCommand("my*file?name");
        command.execute(model);

        Path expectedPath = Path.of("data/myfilename.csv");
        assertTrue(Files.exists(expectedPath),
                "Invalid characters should be removed, creating myfilename.csv");
    }

    @Test
    public void execute_filenameWithLeadingTrailingDots_removesDots() throws CommandException, IOException {
        ExportCommand command = new ExportCommand("..filename..");
        command.execute(model);

        Path expectedPath = Path.of("data/filename.csv");
        assertTrue(Files.exists(expectedPath), "Leading/trailing dots should be removed");
    }

    @Test
    public void execute_filenameWithSpaces_preservesSpaces() throws CommandException, IOException {
        ExportCommand command = new ExportCommand("my file name");
        command.execute(model);

        Path expectedPath = Path.of("data/my file name.csv");
        assertTrue(Files.exists(expectedPath), "Spaces in filename should be preserved");
    }

    @Test
    public void execute_filenameWithUnderscores_preservesUnderscores() throws CommandException, IOException {
        ExportCommand command = new ExportCommand("my_file_name");
        command.execute(model);

        Path expectedPath = Path.of("data/my_file_name.csv");
        assertTrue(Files.exists(expectedPath), "Underscores should be preserved");
    }

    @Test
    public void execute_filenameWithHyphens_preservesHyphens() throws CommandException, IOException {
        ExportCommand command = new ExportCommand("my-file-name");
        command.execute(model);

        Path expectedPath = Path.of("data/my-file-name.csv");
        assertTrue(Files.exists(expectedPath), "Hyphens should be preserved");
    }

    @Test
    public void execute_fileAlreadyExists_createsIncrementedFilename() throws CommandException, IOException {
        // Create first file
        ExportCommand command1 = new ExportCommand("testfile");
        command1.execute(model);

        // Try to create same file again
        ExportCommand command2 = new ExportCommand("testfile");
        CommandResult result = command2.execute(model);

        Path firstFile = Path.of("data/testfile.csv");
        Path secondFile = Path.of("data/testfile-1.csv");

        assertTrue(Files.exists(firstFile), "Original file should exist");
        assertTrue(Files.exists(secondFile), "Incremented file should be created");

        // Check that the success message mentions the filename change
        assertTrue(result.getFeedbackToUser().contains("already exists"));
        assertTrue(result.getFeedbackToUser().contains("testfile-1.csv"));
    }

    @Test
    public void execute_multipleConflicts_createsMultipleIncrements() throws CommandException, IOException {
        // Create multiple files with same base name
        new ExportCommand("conflict").execute(model);
        new ExportCommand("conflict").execute(model);
        new ExportCommand("conflict").execute(model);

        assertTrue(Files.exists(Path.of("data/conflict.csv")));
        assertTrue(Files.exists(Path.of("data/conflict-1.csv")));
        assertTrue(Files.exists(Path.of("data/conflict-2.csv")));
    }

    @Test
    public void execute_createsDataDirectory_ifNotExists() throws CommandException, IOException {
        // Ensure data directory doesn't exist
        Path dataDir = Path.of("data");
        if (Files.exists(dataDir)) {
            Files.walk(dataDir)
                    .sorted((a, b) -> -a.compareTo(b))
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            // Ignore
                        }
                    });
            Files.delete(dataDir);
        }

        ExportCommand command = new ExportCommand("newfile");
        command.execute(model);

        assertTrue(Files.exists(dataDir), "Data directory should be created");
        assertTrue(Files.exists(Path.of("data/newfile.csv")), "File should be created in new directory");
    }

    @Test
    public void execute_defaultFilename_success() throws CommandException, IOException {
        ExportCommand command = new ExportCommand();
        CommandResult result = command.execute(model);

        Path expectedPath = Path.of("data/contacts.csv");
        assertTrue(Files.exists(expectedPath), "Default filename should be contacts.csv");
        assertTrue(result.getFeedbackToUser().contains("contacts.csv"));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        ExportCommand command = new ExportCommand("testfile");
        assertThrows(NullPointerException.class, () -> command.execute(null));
    }

    @Test
    public void requiresWrite_returnsFalse() {
        ExportCommand command = new ExportCommand();
        assertFalse(command.requiresWrite());
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        ExportCommand command = new ExportCommand("test.csv");
        assertTrue(command.equals(command));
    }

    @Test
    public void equals_sameFilename_returnsTrue() {
        ExportCommand command1 = new ExportCommand("test");
        ExportCommand command2 = new ExportCommand("test");
        assertEquals(command1, command2);
    }

    @Test
    public void equals_differentFilename_returnsFalse() {
        ExportCommand command1 = new ExportCommand("test1");
        ExportCommand command2 = new ExportCommand("test2");
        assertFalse(command1.equals(command2));
    }

    @Test
    public void equals_null_returnsFalse() {
        ExportCommand command = new ExportCommand("test.csv");
        assertFalse(command.equals(null));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        ExportCommand command = new ExportCommand("test.csv");
        assertFalse(command.equals("not a command"));
    }

    @Test
    public void hashCode_sameFilename_sameHashCode() {
        ExportCommand command1 = new ExportCommand("test");
        ExportCommand command2 = new ExportCommand("test");
        assertEquals(command1.hashCode(), command2.hashCode());
    }

    @Test
    public void execute_showsAbsolutePath_inSuccessMessage() throws CommandException {
        ExportCommand command = new ExportCommand("testfile");
        CommandResult result = command.execute(model);

        String feedback = result.getFeedbackToUser();
        assertTrue(feedback.contains("data"), "Success message should contain data directory");
    }

    @Test
    public void execute_fileExists_showsInfoMessage() throws CommandException, IOException {
        // Create first file
        new ExportCommand("duplicate").execute(model);

        // Create second file with same name
        ExportCommand command = new ExportCommand("duplicate");
        CommandResult result = command.execute(model);

        assertTrue(result.getFeedbackToUser().contains("already exists"));
        assertTrue(result.getFeedbackToUser().contains("duplicate-1.csv"));
    }

    @Test
    public void registerHelp_success() {
        // Clear registry first
        CommandRegistry.clear();

        // Register help
        ExportCommand.registerHelp();

        // Verify registration
        assertTrue(CommandRegistry.hasCommand(ExportCommand.COMMAND_WORD));
        String help = CommandRegistry.getCommandHelp(ExportCommand.COMMAND_WORD);
        assertTrue(help.contains("export"));
        assertTrue(help.contains("CSV"));
    }
}
