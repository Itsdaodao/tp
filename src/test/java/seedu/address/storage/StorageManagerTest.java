package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.AddressBook;
import seedu.address.model.CommandHistory;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyCommandHistory;
import seedu.address.model.UserPrefs;

public class StorageManagerTest {

    @TempDir
    public Path testFolder;

    private StorageManager storageManager;
    private JsonAddressBookStorage addressBookStorage;
    private JsonUserPrefsStorage userPrefsStorage;

    @BeforeEach
    public void setUp() {
        addressBookStorage = new JsonAddressBookStorage(getTempFilePath("ab"));
        userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        CommandHistoryStorage commandHistoryStorage =
            new NewlineDelimitedCommandHistoryStorage(getTempFilePath("hist"));
        storageManager = new StorageManager(addressBookStorage, userPrefsStorage, commandHistoryStorage);
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.resolve(fileName);
    }

    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(new GuiSettings(300, 600, 4, 6));
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void addressBookReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonAddressBookStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonAddressBookStorageTest} class.
         */
        AddressBook original = getTypicalAddressBook();
        storageManager.saveAddressBook(original);
        ReadOnlyAddressBook retrieved = storageManager.readAddressBook().get();
        assertEquals(original, new AddressBook(retrieved));
    }

    @Test
    public void commandHistoryReadSave() throws Exception {
        CommandHistory original = new CommandHistory();
        original.addCommandToHistory("hi!");
        storageManager.saveCommandHistory(original);
        ReadOnlyCommandHistory retrieved = storageManager.readCommandHistory().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void getCommandHistoryFilePath() {
        assertNotNull(storageManager.getCommandHistoryFilePath());
    }

    @Test
    public void getAddressBookFilePath() {
        assertNotNull(storageManager.getAddressBookFilePath());
    }

    @Test
    public void exportAddressBookToCsv_validPath_success() throws Exception {
        // Setup
        ReadOnlyAddressBook addressBook = getTypicalAddressBook();
        Path csvPath = testFolder.resolve("export.csv");

        // Execute
        storageManager.exportAddressBookToCsv(addressBook, csvPath);

        // Verify
        assertTrue(Files.exists(csvPath));
        String content = Files.readString(csvPath);
        assertTrue(content.contains("Name,Phone,Email,Telegram,GitHub,Tags"));
        assertTrue(content.contains("Alice"));
    }

    @Test
    public void exportAddressBookToCsv_nullAddressBook_throwsNullPointerException() {
        Path csvPath = testFolder.resolve("export.csv");

        assertThrows(NullPointerException.class, () ->
                storageManager.exportAddressBookToCsv(null, csvPath));
    }

    @Test
    public void exportAddressBookToCsv_nullFilePath_throwsNullPointerException() {
        ReadOnlyAddressBook addressBook = getTypicalAddressBook();

        assertThrows(NullPointerException.class, () ->
                storageManager.exportAddressBookToCsv(addressBook, null));
    }

    @Test
    public void exportAddressBookToCsv_invalidPath_throwsException() {
        ReadOnlyAddressBook addressBook = getTypicalAddressBook();
        Path invalidPath = Path.of(""); // Invalid empty path

        assertThrows(IOException.class, () ->
                storageManager.exportAddressBookToCsv(addressBook, invalidPath));
    }

    @Test
    public void exportAddressBookToCsv_createsParentDirectories() throws Exception {
        // Setup - create a path with non-existent parent directories
        ReadOnlyAddressBook addressBook = getTypicalAddressBook();
        Path nestedPath = testFolder.resolve("nested/folder/export.csv");

        // Verify parent directories don't exist initially
        assertTrue(Files.notExists(nestedPath.getParent()));

        // Execute
        storageManager.exportAddressBookToCsv(addressBook, nestedPath);

        // Verify
        assertTrue(Files.exists(nestedPath));
        assertTrue(Files.exists(nestedPath.getParent()));
    }

    @Test
    public void exportAddressBookToCsv_emptyAddressBook_createsFileWithHeader() throws Exception {
        // Setup
        ReadOnlyAddressBook emptyAddressBook = new AddressBook();
        Path csvPath = testFolder.resolve("empty.csv");

        // Execute
        storageManager.exportAddressBookToCsv(emptyAddressBook, csvPath);

        // Verify
        assertTrue(Files.exists(csvPath));
        String content = Files.readString(csvPath);
        assertEquals("Name,Phone,Email,Telegram,GitHub,Tags,Preferred Mode,Pinned,PinnedAt", content.trim());
    }

    @Test
    public void getUserPrefsFilePath_returnsCorrectPath() {
        Path expectedPath = userPrefsStorage.getUserPrefsFilePath();
        Path actualPath = storageManager.getUserPrefsFilePath();

        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void readAddressBook_withCustomPath_success() throws Exception {
        // Setup
        AddressBook original = getTypicalAddressBook();
        Path customPath = testFolder.resolve("custom.json");
        storageManager.saveAddressBook(original, customPath);

        // Execute
        ReadOnlyAddressBook retrieved = storageManager.readAddressBook(customPath).get();

        // Verify
        assertEquals(original, new AddressBook(retrieved));
    }

    @Test
    public void saveAddressBook_withCustomPath_success() throws Exception {
        // Setup
        AddressBook original = getTypicalAddressBook();
        Path customPath = testFolder.resolve("custom.json");

        // Execute
        storageManager.saveAddressBook(original, customPath);

        // Verify
        ReadOnlyAddressBook retrieved = storageManager.readAddressBook(customPath).get();
        assertEquals(original, new AddressBook(retrieved));
    }

    @Test
    public void exportAddressBookToCsv_fileAlreadyExists_overwrites() throws Exception {
        // Setup
        ReadOnlyAddressBook addressBook = getTypicalAddressBook();
        Path csvPath = testFolder.resolve("export.csv");

        // Create file with initial content
        Files.writeString(csvPath, "old content");
        assertTrue(Files.exists(csvPath));

        // Execute
        storageManager.exportAddressBookToCsv(addressBook, csvPath);

        // Verify file was overwritten
        String content = Files.readString(csvPath);
        assertTrue(content.contains("Name,Phone,Email,Telegram,GitHub,Tags"));
        assertTrue(!content.contains("old content"));
    }
}
