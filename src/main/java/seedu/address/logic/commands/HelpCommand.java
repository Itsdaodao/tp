package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n"
            + "Parameters: [COMMAND_NAME]\n"
            + "Example: " + COMMAND_WORD + "\n"
            + "Example: " + COMMAND_WORD + " add";

    private final String targetCommand;

    /**
     * Creates a HelpCommand to show general help for all commands.
     */
    public HelpCommand() {
        this.targetCommand = null;
    }

    /**
     * Creates a HelpCommand to show detailed help for a specific command.
     *
     * @param targetCommand The command to show detailed help for
     */
    public HelpCommand(String targetCommand) {
        requireNonNull(targetCommand);
        this.targetCommand = targetCommand;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        if (targetCommand == null) {
            // Show general help for all commands
            return new CommandResult(CommandRegistry.getAllCommandsHelp());
        } else {
            // Show detailed help for specific command
            if (!CommandRegistry.hasCommand(targetCommand)) {
                throw new CommandException("Unknown command: " + targetCommand
                        + "\n\nUse 'help' to see all available commands.");
            }
            return new CommandResult(CommandRegistry.getCommandHelp(targetCommand));
        }
    }

    @Override
    public boolean requiresWrite() {
        return false;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof HelpCommand)) {
            return false;
        }

        HelpCommand otherHelpCommand = (HelpCommand) other;
        if (targetCommand == null) {
            return otherHelpCommand.targetCommand == null;
        }
        return targetCommand.equals(otherHelpCommand.targetCommand);
    }

    public static void registerHelp() {
        CommandRegistry.register(
                COMMAND_WORD,
                "Shows program usage instructions",
                "Example: help\n"
                        + "            Example: help add",
                "Usage: help [COMMAND_NAME]\n\n"
                        + "Shows general help for all commands, or detailed help for a specific command.\n\n"
                        + "Parameters:\n"
                        + "  COMMAND_NAME (optional) - The name of the command to get detailed help for\n\n"
                        + "Notes:\n"
                        + "  - Without parameters, displays a list of all available commands\n"
                        + "  - With a command name, displays detailed usage information for that command\n"
                        + "  - Command names are case-insensitive"
        );
    }
}
