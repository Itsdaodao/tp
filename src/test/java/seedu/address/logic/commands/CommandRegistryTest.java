package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link CommandRegistry}.
 */
public class CommandRegistryTest {

    @BeforeEach
    @AfterEach
    public void clearRegistry() {
        CommandRegistry.clear();
    }

    @Test
    public void register_commandWithBasicInfo_success() {
        CommandRegistry.register("test", "Test command", "test example");

        assertTrue(CommandRegistry.hasCommand("test"));
        assertEquals("Unknown command: unknown\n\nUse 'help' to see all available commands.",
                CommandRegistry.getCommandHelp("unknown"));
    }

    @Test
    public void register_commandWithDetailedUsage_success() {
        String detailedUsage = "Detailed usage information for test command";
        CommandRegistry.register("test", "Test command", "test example", detailedUsage);

        assertTrue(CommandRegistry.hasCommand("test"));
        String helpText = CommandRegistry.getCommandHelp("test");
        assertTrue(helpText.contains("Command: test"));
        assertTrue(helpText.contains("Description: Test command"));
        assertTrue(helpText.contains(detailedUsage));
    }

    @Test
    public void register_overrideExistingCommand_success() {
        CommandRegistry.register("test", "First description", "first example");
        CommandRegistry.register("test", "Second description", "second example");

        String helpText = CommandRegistry.getCommandHelp("test");
        assertTrue(helpText.contains("Second description"));
        assertFalse(helpText.contains("First description"));
    }

    @Test
    public void hasCommand_existingCommand_returnsTrue() {
        CommandRegistry.register("test", "Test command", "test example");

        assertTrue(CommandRegistry.hasCommand("test"));
    }

    @Test
    public void hasCommand_nonExistingCommand_returnsFalse() {
        assertFalse(CommandRegistry.hasCommand("nonexistent"));
    }

    @Test
    public void hasCommand_caseSensitive_returnsFalseForWrongCase() {
        CommandRegistry.register("test", "Test command", "test example");

        assertFalse(CommandRegistry.hasCommand("TEST"));
        assertFalse(CommandRegistry.hasCommand("Test"));
    }

    @Test
    public void getCommandHelp_existingCommand_returnsHelpText() {
        CommandRegistry.register("test", "Test description", "test example");

        String helpText = CommandRegistry.getCommandHelp("test");
        assertTrue(helpText.contains("Command: test"));
        assertTrue(helpText.contains("Description: Test description"));
        assertTrue(helpText.contains("test example"));
    }

    @Test
    public void getCommandHelp_existingCommandWithDetailedUsage_returnsDetailedHelp() {
        String detailedUsage = "Usage: test [OPTIONS]\nOptions:\n  -v, --verbose  Enable verbose mode";
        CommandRegistry.register("test", "Test command", "test example", detailedUsage);

        String helpText = CommandRegistry.getCommandHelp("test");
        assertTrue(helpText.contains("Command: test"));
        assertTrue(helpText.contains(detailedUsage));
    }

    @Test
    public void getCommandHelp_nonExistingCommand_returnsErrorMessage() {
        String helpText = CommandRegistry.getCommandHelp("unknown");

        assertEquals("Unknown command: unknown\n\nUse 'help' to see all available commands.", helpText);
    }

    @Test
    public void getAllCommandsHelp_noCommands_returnsHeaderOnly() {
        String allHelp = CommandRegistry.getAllCommandsHelp();

        assertEquals("Available commands:\n\n", allHelp);
    }

    @Test
    public void getAllCommandsHelp_multipleCommands_returnsSortedList() {
        CommandRegistry.register("zeta", "Zeta command", "zeta example");
        CommandRegistry.register("alpha", "Alpha command", "alpha example");
        CommandRegistry.register("beta", "Beta command", "beta example");

        String allHelp = CommandRegistry.getAllCommandsHelp();

        // Check that commands are sorted alphabetically
        int alphaIndex = allHelp.indexOf("alpha");
        int betaIndex = allHelp.indexOf("beta");
        int zetaIndex = allHelp.indexOf("zeta");

        assertTrue(alphaIndex < betaIndex);
        assertTrue(betaIndex < zetaIndex);
    }

    @Test
    public void getAllCommandsHelp_commandsWithExamples_includesExamples() {
        CommandRegistry.register("test", "Test command", "test example");

        String allHelp = CommandRegistry.getAllCommandsHelp();

        assertTrue(allHelp.contains("test"));
        assertTrue(allHelp.contains("Test command"));
        assertTrue(allHelp.contains("test example"));
    }

    @Test
    public void getAllCommandsHelp_commandsWithoutExamples_excludesExamples() {
        CommandRegistry.register("test", "Test command", "");

        String allHelp = CommandRegistry.getAllCommandsHelp();

        assertTrue(allHelp.contains("test"));
        assertTrue(allHelp.contains("Test command"));
        // Should not have the indented example line if example is empty
        assertFalse(allHelp.contains("\n            "));
    }

    @Test
    public void clear_removesAllCommands() {
        CommandRegistry.register("test1", "Test command 1", "example1");
        CommandRegistry.register("test2", "Test command 2", "example2");

        assertTrue(CommandRegistry.hasCommand("test1"));
        assertTrue(CommandRegistry.hasCommand("test2"));

        CommandRegistry.clear();

        assertFalse(CommandRegistry.hasCommand("test1"));
        assertFalse(CommandRegistry.hasCommand("test2"));
        assertEquals("Available commands:\n\n", CommandRegistry.getAllCommandsHelp());
    }

    @Test
    public void commandInfo_constructorBasicInfo_setsFieldsCorrectly() {
        CommandRegistry.CommandInfo info = new CommandRegistry.CommandInfo("test", "description", "example");

        assertEquals("test", info.getCommandWord());
        assertEquals("description", info.getDescription());
        assertEquals("example", info.getExample());
        assertFalse(info.hasDetailedUsage());
    }

    @Test
    public void commandInfo_constructorWithDetailedUsage_setsFieldsCorrectly() {
        CommandRegistry.CommandInfo info = new CommandRegistry.CommandInfo("test",
                "description", "example", "detailed");

        assertEquals("test", info.getCommandWord());
        assertEquals("description", info.getDescription());
        assertEquals("example", info.getExample());
        assertEquals("detailed", info.getDetailedUsage());
        assertTrue(info.hasDetailedUsage());
    }

    @Test
    public void commandInfo_hasDetailedUsageTrue() {
        CommandRegistry.CommandInfo info = new CommandRegistry.CommandInfo("test", "desc", "example", "detailed");

        assertTrue(info.hasDetailedUsage());
    }

    @Test
    public void commandInfo_hasDetailedUsageFalse() {
        CommandRegistry.CommandInfo info = new CommandRegistry.CommandInfo("test", "desc", "example");

        assertFalse(info.hasDetailedUsage());
    }

    @Test
    public void commandInfo_hasDetailedUsageEmpty() {
        CommandRegistry.CommandInfo info = new CommandRegistry.CommandInfo("test", "desc", "example", "");

        assertFalse(info.hasDetailedUsage());
    }

    @Test
    public void commandInfo_hasDetailedUsageNull() {
        CommandRegistry.CommandInfo info = new CommandRegistry.CommandInfo("test", "desc", "example", null);

        assertFalse(info.hasDetailedUsage());
    }

    @Test
    public void initialize_registersAllCommands() {
        // Clear any existing registrations
        CommandRegistry.clear();

        // Initialize should register all commands from COMMAND_CLASS_NAMES
        CommandRegistry.initialize();

        // Check that some expected commands are registered
        assertTrue(CommandRegistry.hasCommand("help"));
        assertTrue(CommandRegistry.hasCommand("add"));
        assertTrue(CommandRegistry.hasCommand("list"));
        assertTrue(CommandRegistry.hasCommand("exit"));
    }

    @Test
    public void formatCommandHelp_withExample_includesExample() {
        CommandRegistry.CommandInfo info = new CommandRegistry.CommandInfo("test", "Test description", "test example");

        String formatted = getFormattedCommandHelp(info);

        assertTrue(formatted.contains("test       "));
        assertTrue(formatted.contains("Test description"));
        assertTrue(formatted.contains("\n            test example"));
    }

    @Test
    public void formatCommandHelp_withoutExample_excludesExampleLine() {
        CommandRegistry.CommandInfo info = new CommandRegistry.CommandInfo("test", "Test description", "");

        String formatted = getFormattedCommandHelp(info);

        assertTrue(formatted.contains("test       "));
        assertTrue(formatted.contains("Test description"));
        assertFalse(formatted.contains("\n            "));
    }

    @Test
    public void formatCommandHelp_withNullExample_excludesExampleLine() {
        CommandRegistry.CommandInfo info = new CommandRegistry.CommandInfo("test", "Test description", null);

        String formatted = getFormattedCommandHelp(info);

        assertTrue(formatted.contains("test       "));
        assertTrue(formatted.contains("Test description"));
        assertFalse(formatted.contains("\n            "));
    }

    @Test
    public void formatDetailedCommandHelp_withDetailedUsage_includesDetailedUsage() {
        String detailedUsage = "Detailed usage information";
        CommandRegistry.CommandInfo info = new CommandRegistry.CommandInfo("test",
                "Test description", "example", detailedUsage);

        String formatted = getFormattedDetailedCommandHelp(info);

        assertTrue(formatted.contains("Command: test"));
        assertTrue(formatted.contains("Description: Test description"));
        assertTrue(formatted.contains(detailedUsage));
        assertTrue(formatted.contains("example"));
    }

    @Test
    public void formatDetailedCommandHelp_withoutDetailedUsage_excludesDetailedUsageSection() {
        CommandRegistry.CommandInfo info = new CommandRegistry.CommandInfo("test", "Test description", "example");

        String formatted = getFormattedDetailedCommandHelp(info);

        assertTrue(formatted.contains("Command: test"));
        assertTrue(formatted.contains("Description: Test description"));
        assertTrue(formatted.contains("example"));
        // Should not have extra newlines from missing detailed usage
        assertFalse(formatted.contains("\n\n\n"));
    }

    @Test
    public void formatDetailedCommandHelp_withoutExample_excludesExample() {
        CommandRegistry.CommandInfo info = new CommandRegistry.CommandInfo("test", "Test description", "");

        String formatted = getFormattedDetailedCommandHelp(info);

        assertTrue(formatted.contains("Command: test"));
        assertTrue(formatted.contains("Description: Test description"));
        // Should end with description without extra example
        assertTrue(formatted.trim().endsWith("Test description"));
    }

    // Helper methods to access private formatting methods via reflection
    private String getFormattedCommandHelp(CommandRegistry.CommandInfo info) {
        try {
            var method = CommandRegistry.class.getDeclaredMethod("formatCommandHelp",
                    CommandRegistry.CommandInfo.class);
            method.setAccessible(true);
            return (String) method.invoke(null, info);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getFormattedDetailedCommandHelp(CommandRegistry.CommandInfo info) {
        try {
            var method = CommandRegistry.class.getDeclaredMethod("formatDetailedCommandHelp",
                    CommandRegistry.CommandInfo.class);
            method.setAccessible(true);
            return (String) method.invoke(null, info);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void initialize_registersExportCommand() {
        CommandRegistry.clear();
        CommandRegistry.initialize();

        assertTrue(CommandRegistry.hasCommand("export"));
        String helpText = CommandRegistry.getCommandHelp("export");
        assertTrue(helpText.contains("export"));
        assertTrue(helpText.contains("CSV"));
    }

    @Test
    public void getCommandFactory_exportCommand_returnsFactory() {
        CommandRegistry.initialize();

        CommandFactory factory = CommandRegistry.getCommandFactory("export");
        assertTrue(factory != null);
    }

    @Test
    public void getCommandFactory_exportCommandWithArgs_createsCommand() throws Exception {
        CommandRegistry.initialize();

        CommandFactory factory = CommandRegistry.getCommandFactory("export");
        Command command = factory.create("test.csv");

        assertTrue(command instanceof ExportCommand);
    }

    @Test
    public void getCommandFactory_exportCommandEmptyArgs_createsCommand() throws Exception {
        CommandRegistry.initialize();

        CommandFactory factory = CommandRegistry.getCommandFactory("export");
        Command command = factory.create("");

        assertTrue(command instanceof ExportCommand);
    }
}
