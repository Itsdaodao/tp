package seedu.address.model;

import java.util.List;

/**
 * Unmodifiable view of the Command History
 */
public interface ReadOnlyCommandHistory {

    /**
     * Returns the next (more recent) command in the history.
     */
    String getNextCommandFromHistory();

    /**
     * Returns the previous (less recent) command in the history.
     */
    String getPreviousCommandFromHistory(String curr);

    /**
     * Returns the whole history list.
     */
    List<String> getHistory();
}
