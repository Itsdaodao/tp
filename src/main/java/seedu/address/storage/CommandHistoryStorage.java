package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.CommandHistory;
import seedu.address.model.ReadOnlyCommandHistory;

// move command history model to model package?

public interface CommandHistoryStorage {
    /**
     * Returns the file path of the UserPrefs data file.
     */
    Path getCommandHistoryFilePath();

    /**
     * Returns UserPrefs data from storage.
     * Returns {@code Optional.empty()} if storage file is not found.
     *
     * @throws DataLoadingException if the loading of data from preference file failed.
     */
    Optional<CommandHistory> readCommandHistory() throws DataLoadingException;

    /**
     * Saves the given {@link seedu.address.model.ReadOnlyUserPrefs} to the storage.
     * @param userPrefs cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveCommandHistory(ReadOnlyCommandHistory history) throws IOException;
}
