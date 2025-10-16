package seedu.address.logic.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Maintains a registry of all available commands and their help information.
 * This class acts as a central repository where commands can register themselves
 * to provide consistent help documentation across the application.
 */
public class CommandRegistry {
    private static final Map<String, CommandInfo> commands = new HashMap<>();

    /**
     * Represents information about a command including its word, description, and usage example.
     */
    public static class CommandInfo {
        private final String commandWord;
        private final String description;
        private final String example;

        /**
         * Constructs a CommandInfo with the specified details.
         *
         * @param commandWord The command word used to invoke this command
         * @param description A brief description of what the command does
         * @param example An example showing how to use the command
         */
        public CommandInfo(String commandWord, String description, String example) {
            this.commandWord = commandWord;
            this.description = description;
            this.example = example;
        }

        /**
         * Returns the command word for this command.
         *
         * @return The command word as a {@code String}
         */
        public String getCommandWord() {
            return commandWord;
        }

        /**
         * Returns the description of what this command does.
         *
         * @return The description as a {@code String}
         */
        public String getDescription() {
            return description;
        }

        /**
         * Returns an example showing how to use this command.
         *
         * @return The usage example as a {@code String}, or an empty string if no example is provided
         */
        public String getExample() {
            return example;
        }
    }

    /**
     * Registers a command with its help information in the registry.
     * If a command with the same command word already exists, it will be replaced.
     *
     * @param commandWord The command word used to invoke the command
     * @param description A brief description of what the command does
     * @param example An example showing how to use the command, or an empty string if no example is needed
     */
    public static void register(String commandWord, String description, String example) {
        commands.put(commandWord, new CommandInfo(commandWord, description, example));
    }

    /**
     * Returns the help text for all registered commands.
     * Commands are sorted alphabetically by their command word.
     * Each command's help information is formatted consistently with proper spacing and indentation.
     *
     * @return A formatted {@code String} containing help information for all commands
     */
    public static String getAllCommandsHelp() {
        return "Available commands:\n\n" + commands.values().stream()
                .sorted((a, b) -> a.getCommandWord().compareTo(b.getCommandWord()))
                .map(CommandRegistry::formatCommandHelp)
                .collect(Collectors.joining("\n"));
    }

    /**
     * Returns the help text for a specific command.
     * If the command is not found in the registry, returns an error message.
     *
     * @param commandWord The command word to get help information for
     * @return A formatted {@code String} containing help information for the specified command,
     *         or an error message if the command is not registered
     */
    public static String getCommandHelp(String commandWord) {
        CommandInfo info = commands.get(commandWord);
        if (info == null) {
            return "Unknown command: " + commandWord;
        }
        return formatCommandHelp(info);
    }

    /**
     * Formats the help information for a single command.
     * The format includes the command word aligned to 12 characters, followed by the description.
     * If an example is provided, it is displayed on a new line with proper indentation.
     *
     * @param info The {@code CommandInfo} containing the command's help information
     * @return A formatted {@code String} representing the command's help text
     */
    private static String formatCommandHelp(CommandInfo info) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-12s", info.getCommandWord()))
                .append(info.getDescription());
        if (info.getExample() != null && !info.getExample().isEmpty()) {
            sb.append("\n            ").append(info.getExample());
        }
        return sb.toString();
    }

    /**
     * Clears all registered commands from the registry.
     * This method is primarily useful for testing purposes to ensure a clean state between tests.
     */
    public static void clear() {
        commands.clear();
    }
}