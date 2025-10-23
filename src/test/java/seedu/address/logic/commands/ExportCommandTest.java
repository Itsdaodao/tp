package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.commands.exceptions.CommandException;
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
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        testFilePath = temporaryFolder.resolve("testexport.csv");
    }

    @AfterEach
    public void tearDown() throws IOException {
        // Clean up test files
        if (Files.exists(testFilePath)) {
            Files.delete(testFilePath);
        }
    }

    @Test
    public void constructor_nullFilePath_usesDefaultPath() {
        ExportCommand command = new ExportCommand(null);
        assertEquals(new ExportCommand(), command);
    }

    @Test
    public void constructor_emptyFilePath_usesDefaultPath() {
        ExportCommand command = new ExportCommand("");
        assertEquals(new ExportCommand(), command);
    }

    @Test
    public void constructor_whitespaceFilePath_usesDefaultPath() {
        ExportCommand command = new ExportCommand("   ");
        assertEquals(new ExportCommand(), command);
    }

    @Test
    public void constructor_validFilePath_success() {
        ExportCommand command = new ExportCommand("custom.csv");
        ExportCommand otherCommand = new ExportCommand("custom.csv");
        assertEquals(command, otherCommand);
    }

    @Test
    public void execute_validPath_success() throws CommandException, IOException {
        ExportCommand command = new ExportCommand(testFilePath.toString());
        CommandResult result = command.execute(model);

        // Check that file was created
        assertTrue(Files.exists(testFilePath));

        // Check success message
        assertTrue(result.getFeedbackToUser().contains("Contacts exported successfully"));
        assertTrue(result.getFeedbackToUser().contains(testFilePath.toAbsolutePath().toString()));
    }

    @Test
    public void execute_defaultPath_success() throws CommandException, IOException {
        ExportCommand command = new ExportCommand();
        CommandResult result = command.execute(model);

        // Check success message
        assertTrue(result.getFeedbackToUser().contains("Contacts exported successfully"));
        assertTrue(result.getFeedbackToUser().contains("data/contacts.csv"));

        // Clean up
        Path defaultPath = Paths.get("data/contacts.csv");
        if (Files.exists(defaultPath)) {
            Files.delete(defaultPath);
            // Try to delete parent directory if empty
            try {
                Files.deleteIfExists(defaultPath.getParent());
            } catch (IOException e) {
                // Ignore if directory not empty
            }
        }
    }

    @Test
    public void execute_invalidPath_throwsCommandException() {
        // Use an invalid path (contains illegal characters on most systems)
        String invalidPath = "\0invalid.csv";
        ExportCommand command = new ExportCommand(invalidPath);

        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        ExportCommand command = new ExportCommand(testFilePath.toString());
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
    public void equals_sameFilePath_returnsTrue() {
        ExportCommand command1 = new ExportCommand("test.csv");
        ExportCommand command2 = new ExportCommand("test.csv");
        assertTrue(command1.equals(command2));
    }

    @Test
    public void equals_differentFilePath_returnsFalse() {
        ExportCommand command1 = new ExportCommand("test1.csv");
        ExportCommand command2 = new ExportCommand("test2.csv");
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

    @Test
    public void execute_fileAlreadyExists_overwritesFile() throws CommandException, IOException {
        // Create file first
        Files.writeString(testFilePath, "old content");
        assertTrue(Files.exists(testFilePath));

        // Execute export
        ExportCommand command = new ExportCommand(testFilePath.toString());
        command.execute(model);

        // Check that file was overwritten (not same as old content)
        String content = Files.readString(testFilePath);
        assertFalse(content.equals("old content"));
        assertTrue(content.contains("Name,Phone,Email"));
    }

    @Test
    public void execute_createsParentDirectories() throws CommandException, IOException {
        Path nestedPath = temporaryFolder.resolve("nested/folder/export.csv");
        assertFalse(Files.exists(nestedPath.getParent()));

        ExportCommand command = new ExportCommand(nestedPath.toString());
        command.execute(model);

        assertTrue(Files.exists(nestedPath));
        assertTrue(Files.exists(nestedPath.getParent()));
    }
}
