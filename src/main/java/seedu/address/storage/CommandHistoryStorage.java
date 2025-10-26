package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.CommandHistory;
import seedu.address.model.ReadOnlyCommandHistory;

/**
 * Represents a storage for {@link seedu.address.model.CommandHistory}.
 */
public interface CommandHistoryStorage {
    /**
     * Returns the file path of the CommandHistory data file.
     */
    Path getCommandHistoryFilePath();

    /**
     * Returns CommandHistory data from storage.
     * Returns {@code Optional.empty()} if storage file is not found.
     *
     * @throws DataLoadingException if the loading of data from history file failed.
     */
    Optional<CommandHistory> readCommandHistory() throws DataLoadingException;

    /**
     * Saves the given {@link seedu.address.model.ReadOnlyCommandHistory} to the storage.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveCommandHistory(ReadOnlyCommandHistory history) throws IOException;
}
