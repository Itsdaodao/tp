package seedu.address.model;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CommandHistory implements ReadOnlyCommandHistory {
    private final LinkedList<String> commandHistory;
    private final int HISTORY_CAPACITY = 15;
    private int indexInHistory = -1;
    private String currentCommand = "";


    /**
     * Creates a {@code CommandHistoryManager} with default capacity of 15 commands.
     */
    public CommandHistory() {
        this.commandHistory = new LinkedList<>();
    }

    public CommandHistory(ReadOnlyCommandHistory commandHistory) {
        this.commandHistory = new LinkedList<>();
        this.commandHistory.addAll(commandHistory.getHistory());
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
    @Override
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
    @Override
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

    @Override
    public List<String> getHistory() {
        return List.copyOf(this.commandHistory);
    }


    public void clearHistory() {
        commandHistory.clear();
        indexInHistory = -1;
        currentCommand = "";
    }
}
