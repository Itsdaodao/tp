package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.HOON;
import static seedu.address.testutil.TypicalPersons.IDA;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;

public class JsonAddressBookStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonAddressBookStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readAddressBook_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readAddressBook(null));
    }

    private java.util.Optional<ReadOnlyAddressBook> readAddressBook(String filePath) throws Exception {
        return new JsonAddressBookStorage(Paths.get(filePath)).readAddressBook(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readAddressBook("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataLoadingException.class, () -> readAddressBook("notJsonFormatAddressBook.json"));
    }

    @Test
    public void readAddressBook_invalidPersonAddressBook_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readAddressBook("invalidPersonAddressBook.json"));
    }

    @Test
    public void readAddressBook_invalidAndValidPersonAddressBook_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readAddressBook("invalidAndValidPersonAddressBook.json"));
    }

    @Test
    public void readAndSaveAddressBook_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempAddressBook.json");
        AddressBook original = getTypicalAddressBook();
        JsonAddressBookStorage jsonAddressBookStorage = new JsonAddressBookStorage(filePath);

        // Save in new file and read back
        jsonAddressBookStorage.saveAddressBook(original, filePath);
        ReadOnlyAddressBook readBack = jsonAddressBookStorage.readAddressBook(filePath).get();
        assertEquals(original, new AddressBook(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addPerson(HOON);
        original.removePerson(ALICE);
        jsonAddressBookStorage.saveAddressBook(original, filePath);
        readBack = jsonAddressBookStorage.readAddressBook(filePath).get();
        assertEquals(original, new AddressBook(readBack));

        // Save and read without specifying file path
        original.addPerson(IDA);
        jsonAddressBookStorage.saveAddressBook(original); // file path not specified
        readBack = jsonAddressBookStorage.readAddressBook().get(); // file path not specified
        assertEquals(original, new AddressBook(readBack));
    }

    @Test
    public void saveAddressBook_nullAddressBook_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveAddressBook(null, "SomeFile.json"));
    }

    /**
     * Saves {@code addressBook} at the specified {@code filePath}.
     */
    private void saveAddressBook(ReadOnlyAddressBook addressBook, String filePath) {
        try {
            new JsonAddressBookStorage(Paths.get(filePath))
                    .saveAddressBook(addressBook, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveAddressBook_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveAddressBook(new AddressBook(), null));
    }

    @Test
    public void getAddressBookFilePath_returnsCorrectPath() {
        Path expectedPath = Paths.get("test.json");
        JsonAddressBookStorage storage = new JsonAddressBookStorage(expectedPath);
        assertEquals(expectedPath, storage.getAddressBookFilePath());
    }

    @Test
    public void exportAddressBookToCsv_validPath_success() throws Exception {
        // Setup
        Path jsonFilePath = testFolder.resolve("test.json");
        Path csvFilePath = testFolder.resolve("export.csv");
        JsonAddressBookStorage storage = new JsonAddressBookStorage(jsonFilePath);
        ReadOnlyAddressBook addressBook = getTypicalAddressBook();

        // Execute
        storage.exportAddressBookToCsv(addressBook, csvFilePath);

        // Verify
        assertTrue(Files.exists(csvFilePath));
        String content = Files.readString(csvFilePath);
        assertTrue(content.contains("Name,Phone,Email,Telegram,GitHub,Tags"));
        assertTrue(content.contains("Alice"));
    }

    @Test
    public void exportAddressBookToCsv_nullAddressBook_throwsNullPointerException() {
        Path jsonFilePath = testFolder.resolve("test.json");
        Path csvFilePath = testFolder.resolve("export.csv");
        JsonAddressBookStorage storage = new JsonAddressBookStorage(jsonFilePath);

        assertThrows(NullPointerException.class, () ->
                storage.exportAddressBookToCsv(null, csvFilePath));
    }

    @Test
    public void exportAddressBookToCsv_nullFilePath_throwsNullPointerException() {
        Path jsonFilePath = testFolder.resolve("test.json");
        JsonAddressBookStorage storage = new JsonAddressBookStorage(jsonFilePath);
        ReadOnlyAddressBook addressBook = getTypicalAddressBook();

        assertThrows(NullPointerException.class, () ->
                storage.exportAddressBookToCsv(addressBook, null));
    }

    @Test
    public void exportAddressBookToCsv_invalidPath_throwsException() {
        Path jsonFilePath = testFolder.resolve("test.json");
        JsonAddressBookStorage storage = new JsonAddressBookStorage(jsonFilePath);
        ReadOnlyAddressBook addressBook = getTypicalAddressBook();
        Path invalidPath = Path.of(""); // Invalid empty path

        assertThrows(IOException.class, () ->
                storage.exportAddressBookToCsv(addressBook, invalidPath));
    }

    @Test
    public void exportAddressBookToCsv_createsParentDirectories() throws Exception {
        // Setup
        Path jsonFilePath = testFolder.resolve("test.json");
        Path nestedCsvPath = testFolder.resolve("nested/folder/export.csv");
        JsonAddressBookStorage storage = new JsonAddressBookStorage(jsonFilePath);
        ReadOnlyAddressBook addressBook = getTypicalAddressBook();

        // Verify parent directories don't exist initially
        assertTrue(Files.notExists(nestedCsvPath.getParent()));

        // Execute
        storage.exportAddressBookToCsv(addressBook, nestedCsvPath);

        // Verify
        assertTrue(Files.exists(nestedCsvPath));
        assertTrue(Files.exists(nestedCsvPath.getParent()));
    }

    @Test
    public void exportAddressBookToCsv_emptyAddressBook_createsFileWithHeader() throws Exception {
        // Setup
        Path jsonFilePath = testFolder.resolve("test.json");
        Path csvFilePath = testFolder.resolve("empty.csv");
        JsonAddressBookStorage storage = new JsonAddressBookStorage(jsonFilePath);
        ReadOnlyAddressBook emptyAddressBook = new AddressBook();

        // Execute
        storage.exportAddressBookToCsv(emptyAddressBook, csvFilePath);

        // Verify
        assertTrue(Files.exists(csvFilePath));
        String content = Files.readString(csvFilePath);
        assertEquals("Name,Phone,Email,Telegram,GitHub,Tags,Preferred Mode,Pinned,PinnedAt", content.trim());
    }

    @Test
    public void exportAddressBookToCsv_fileAlreadyExists_overwrites() throws Exception {
        // Setup
        Path jsonFilePath = testFolder.resolve("test.json");
        Path csvFilePath = testFolder.resolve("export.csv");
        JsonAddressBookStorage storage = new JsonAddressBookStorage(jsonFilePath);
        ReadOnlyAddressBook addressBook = getTypicalAddressBook();

        // Create file with initial content
        Files.writeString(csvFilePath, "old content");
        assertTrue(Files.exists(csvFilePath));

        // Execute
        storage.exportAddressBookToCsv(addressBook, csvFilePath);

        // Verify file was overwritten
        String content = Files.readString(csvFilePath);
        assertTrue(content.contains("Name,Phone,Email,Telegram,GitHub,Tags"));
        assertFalse(content.contains("old content"));
    }

    @Test
    public void saveAddressBook_defaultPath_success() throws Exception {
        // Setup
        Path defaultFilePath = testFolder.resolve("default.json");
        JsonAddressBookStorage storage = new JsonAddressBookStorage(defaultFilePath);
        AddressBook original = getTypicalAddressBook();

        // Execute - save without specifying file path (should use default)
        storage.saveAddressBook(original);

        // Verify
        ReadOnlyAddressBook readBack = storage.readAddressBook().get();
        assertEquals(original, new AddressBook(readBack));
    }

    @Test
    public void readAddressBook_defaultPath_success() throws Exception {
        // Setup
        Path defaultFilePath = testFolder.resolve("default.json");
        JsonAddressBookStorage storage = new JsonAddressBookStorage(defaultFilePath);
        AddressBook original = getTypicalAddressBook();
        storage.saveAddressBook(original);

        // Execute - read without specifying file path (should use default)
        ReadOnlyAddressBook readBack = storage.readAddressBook().get();

        // Verify
        assertEquals(original, new AddressBook(readBack));
    }
}
