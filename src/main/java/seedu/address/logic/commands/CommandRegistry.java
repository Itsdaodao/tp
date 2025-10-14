package seedu.address.logic.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import seedu.address.logic.parser.AddCommandParser;
import seedu.address.logic.parser.DeleteCommandParser;
import seedu.address.logic.parser.EditCommandParser;
import seedu.address.logic.parser.FindCommandParser;

/**
 * Central Registry for all commands in the application.
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
        commands.put(ListCommand.COMMAND_WORD, (args) -> new ListCommand());
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
