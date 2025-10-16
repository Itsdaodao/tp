package seedu.address.logic.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;

/**
 * Central Registry for parse-related information used in the application.
 */
public class CommandRegistry {
    private static final Map<String, CommandFactory> commands;

    // Instantiate the command map
    static {
        commands = new HashMap<>();
        commands.put(AddCommand.COMMAND_WORD, (args) -> new AddCommandParser().parse(args));
        commands.put(EditCommand.COMMAND_WORD, (args) -> new EditCommandParser().parse(args));
        commands.put(DeleteCommand.COMMAND_WORD, (args) -> new DeleteCommandParser().parse(args));
        commands.put(ClearCommand.COMMAND_WORD, (args) -> new ClearCommand());
        commands.put(FindCommand.COMMAND_WORD, (args) -> new FindCommandParser().parse(args));
        commands.put(ListCommand.COMMAND_WORD, (args) -> new ListCommandParser().parse(args));
        commands.put(ExitCommand.COMMAND_WORD, (args) -> new ExitCommand());
        commands.put(HelpCommand.COMMAND_WORD, (args) -> new HelpCommand());
    }

    /**
     * Returns a set of all valid command words in the application.
     */
    public static Set<String> getCommandWords() {
        return commands.keySet();
    }

    /**
     * Returns the CommandFactory associated with the given command word.
     * Returns null if the command word is not recognized.
     */
    public static CommandFactory getCommandFactory(String commandWord) {
        return commands.get(commandWord);
    }
}
