package seedu.address.ui;

import java.util.LinkedList;

public class CommandHistoryManager {
    private final LinkedList<String> commandHistory;
    private final int HISTORY_CAPACITY;
    private int indexInHistory = -1;
    private String currentCommand = "";


    /**
     * Creates a {@code CommandHistoryManager} with default capacity of 15 commands.
     */
    public CommandHistoryManager() {
        this.commandHistory = new LinkedList<>();
        this.HISTORY_CAPACITY = 15;
    }

    /**
     * Adds a command to the history.
     *
     * @param command The command to be added.
     */
    public void addCommandToHistory(String command) {
        this.commandHistory.addFirst(command);
        this.indexInHistory = -1;
        if (commandHistory.size() > HISTORY_CAPACITY) {
            commandHistory.removeLast();
        }
    }

    /**
     * Retrieves the next command from history.
     *
     * @return The next command, or an empty string if at the end of history.
     */
    public String getNextCommandFromHistory() {
        if (commandHistory.isEmpty()) {
            return null;
        }

        indexInHistory = Math.max(indexInHistory-1, -1);

        if (indexInHistory == -1) {
            return currentCommand;
        } else {
            return commandHistory.get(indexInHistory);
        }
    }

    /**
     * Retrieves the previous command from history.
     *
     * @return The previous command, or an empty string if no recorded history.
     */
    public String getPreviousCommandFromHistory(String currentCommand) {
        if (commandHistory.isEmpty()){
            return null;
        }

        if (indexInHistory == -1) {
            this.currentCommand = currentCommand;
        }

        indexInHistory = Math.min(indexInHistory+1, commandHistory.size() - 1);

        return commandHistory.get(indexInHistory);
    }

    public void clearHistory() {
        commandHistory.clear();
        indexInHistory = -1;
        currentCommand = "";
    }
}
