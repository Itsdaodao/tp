package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.testutil.Assert.assertThrows;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.CommandHistory;

public class NewlineDelimitedCommandHistoryStorageTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data",
        "NewlineDelimitedCommandHistoryStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readCommandHistory_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readCommandHistory(null));
    }

    private Optional<CommandHistory> readCommandHistory(String commandHistoryFileInTestDataFolder)
            throws DataLoadingException {
        Path histFilePath = addToTestDataPathIfNotNull(commandHistoryFileInTestDataFolder);
        return new NewlineDelimitedCommandHistoryStorage(histFilePath).readCommandHistory();
    }

    @Test
    public void readCommandHistory_missingFile_emptyResult() throws DataLoadingException {
        assertFalse(readCommandHistory("NonExistentFile.json").isPresent());
    }

    private Path addToTestDataPathIfNotNull(String userPrefsFileInTestDataFolder) {
        return userPrefsFileInTestDataFolder != null
            ? TEST_DATA_FOLDER.resolve(userPrefsFileInTestDataFolder)
            : null;
    }

    @Test
    public void readCommandHistory_fileInOrder_successfullyRead() throws DataLoadingException {
        CommandHistory expected = getTypicalCommandHistory();
        CommandHistory actual = readCommandHistory(".typical_hist").get();
        assertEquals(expected, actual);
    }

    @Test
    public void readCommandHistory_valuesMissingFromFile_defaultValuesUsed() throws DataLoadingException {
        CommandHistory actual = readCommandHistory(".empty_hist").get();
        assertEquals(new CommandHistory(), actual);
    }

    private CommandHistory getTypicalCommandHistory() {
        CommandHistory commandHistory = new CommandHistory();
        commandHistory.addCommandToHistory("delete 1");
        return commandHistory;
    }

    @Test
    public void saveCommandHistory_nullHistory_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveCommandHistory(null, ".saved_hist"));
    }

    @Test
    public void saveCommandHistory_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveCommandHistory(new CommandHistory(), null));
    }

    /**
     * Saves {@code userPrefs} at the specified {@code prefsFileInTestDataFolder} filepath.
     */
    private void saveCommandHistory(CommandHistory history, String histFileIntTestDataFolder) {
        try {
            new NewlineDelimitedCommandHistoryStorage(addToTestDataPathIfNotNull(histFileIntTestDataFolder))
                .saveCommandHistory(history);
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file", ioe);
        }
    }

    @Test
    public void saveCommandHistory_allInOrder_success() throws DataLoadingException, IOException {

        CommandHistory history = new CommandHistory();
        history.addCommandToHistory("test command");

        Path histFilePath = testFolder.resolve(".save_history");
        NewlineDelimitedCommandHistoryStorage commandHistoryStorage =
            new NewlineDelimitedCommandHistoryStorage(histFilePath);

        //Try writing when the file doesn't exist
        commandHistoryStorage.saveCommandHistory(history);
        CommandHistory readBack = commandHistoryStorage.readCommandHistory().get();
        assertEquals(history, readBack);

        //Try saving when the file exists
        history.addCommandToHistory("exit");
        commandHistoryStorage.saveCommandHistory(history);
        readBack = commandHistoryStorage.readCommandHistory().get();
        assertEquals(history, readBack);
    }


}
