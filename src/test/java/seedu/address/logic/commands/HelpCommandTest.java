package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class HelpCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @BeforeEach
    public void setUp() {
        CommandRegistry.clear();
        // Register commands that we'll test with
        HelpCommand.registerHelp();
        AddCommand.registerHelp();
    }

    @Test
    public void execute_generalHelp_success() {
        HelpCommand helpCommand = new HelpCommand();
        String expectedMessage = CommandRegistry.getAllCommandsHelp();

        assertCommandSuccess(helpCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_specificCommandHelp_success() {
        String commandName = "add";
        HelpCommand helpCommand = new HelpCommand(commandName);
        String expectedMessage = CommandRegistry.getCommandHelp(commandName);

        assertCommandSuccess(helpCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_unknownCommand_throwsCommandException() {
        String unknownCommand = "unknown";
        HelpCommand helpCommand = new HelpCommand(unknownCommand);

        assertThrows(CommandException.class, () -> helpCommand.execute(model));
    }

    @Test
    public void constructor_nullCommand_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new HelpCommand(null));
    }

    @Test
    public void requiresWrite_returnsFalse() {
        HelpCommand helpCommand = new HelpCommand();
        assertFalse(helpCommand.requiresWrite());
    }

    @Test
    public void requiresWrite_specificCommand_returnsFalse() {
        HelpCommand helpCommand = new HelpCommand("add");
        assertFalse(helpCommand.requiresWrite());
    }

    @Test
    public void equals_generalHelpCommands() {
        HelpCommand helpCommand1 = new HelpCommand();
        HelpCommand helpCommand2 = new HelpCommand();

        assertEquals(helpCommand1, helpCommand2);
        assertNotEquals(null, helpCommand1);
    }

    @Test
    public void equals_specificCommandHelpCommands() {
        HelpCommand helpCommand1 = new HelpCommand("add");
        HelpCommand helpCommand2 = new HelpCommand("add");
        HelpCommand helpCommand3 = new HelpCommand("delete");

        assertEquals(helpCommand1, helpCommand2);
        assertNotEquals(helpCommand1, helpCommand3);
        assertNotEquals(new HelpCommand(), helpCommand1);
    }

    @Test
    public void equals_mixedGeneralAndSpecific() {
        HelpCommand generalHelp = new HelpCommand();
        HelpCommand specificHelp = new HelpCommand("add");

        assertNotEquals(generalHelp, specificHelp);
    }
}
