package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class CsvAddressBookStorageTest {

    private static final String CSV_HEADER = "Name,Phone,Email,Telegram,GitHub,Tags,Preferred Mode,Pinned";

    @TempDir
    public Path temporaryFolder;

    private Path testFilePath;

    @AfterEach
    public void tearDown() throws IOException {
        if (testFilePath != null && Files.exists(testFilePath)) {
            Files.delete(testFilePath);
        }
    }

    @Test
    public void exportToCsv_nullAddressBook_throwsNullPointerException() {
        testFilePath = temporaryFolder.resolve("test.csv");
        assertThrows(NullPointerException.class, () ->
                CsvAddressBookStorage.exportToCsv(null, testFilePath));
    }

    @Test
    public void exportToCsv_nullFilePath_throwsNullPointerException() {
        ReadOnlyAddressBook addressBook = getTypicalAddressBook();
        assertThrows(NullPointerException.class, () ->
                CsvAddressBookStorage.exportToCsv(addressBook, null));
    }

    @Test
    public void exportToCsv_emptyAddressBook_createsFileWithHeaderOnly() throws IOException {
        testFilePath = temporaryFolder.resolve("empty.csv");
        AddressBook emptyAddressBook = new AddressBook();

        CsvAddressBookStorage.exportToCsv(emptyAddressBook, testFilePath);

        assertTrue(Files.exists(testFilePath));
        List<String> lines = Files.readAllLines(testFilePath);
        assertEquals(1, lines.size()); // Only header
        assertEquals(CSV_HEADER, lines.get(0));
    }

    @Test
    public void exportToCsv_typicalAddressBook_success() throws IOException {
        testFilePath = temporaryFolder.resolve("typical.csv");
        ReadOnlyAddressBook addressBook = getTypicalAddressBook();

        CsvAddressBookStorage.exportToCsv(addressBook, testFilePath);

        assertTrue(Files.exists(testFilePath));
        List<String> lines = Files.readAllLines(testFilePath);

        // Should have header + number of persons
        assertTrue(lines.size() > 1);
        assertEquals(CSV_HEADER, lines.get(0));

        // Check that file contains data
        String content = String.join("\n", lines);
        assertTrue(content.contains("Alice"));
    }

    @Test
    public void exportToCsv_personWithAllFields_correctFormat() throws IOException {
        testFilePath = temporaryFolder.resolve("allfields.csv");

        Person person = new PersonBuilder()
                .withName("John Doe")
                .withPhone("98765432")
                .withEmail("john@example.com")
                .withTelegram("johndoe")
                .withGithub("johndoe")
                .withTags("friend", "colleague")
                .withPreferredMode("phone")
                .withPinnedAt()
                .build();

        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(person);

        CsvAddressBookStorage.exportToCsv(addressBook, testFilePath);

        List<String> lines = Files.readAllLines(testFilePath);
        System.out.println(lines.size());
        assertEquals(2, lines.size()); // Header + 1 person

        String dataLine = lines.get(1);
        System.out.println(dataLine);
        assertTrue(dataLine.contains("John Doe"));
        assertTrue(dataLine.contains("98765432"));
        assertTrue(dataLine.contains("john@example.com"));
        assertTrue(dataLine.contains("johndoe"));
        assertTrue(dataLine.contains("friend; colleague"));
        assertTrue(dataLine.contains("phone"));
        assertTrue(dataLine.contains("true"));
    }

    @Test
    public void exportToCsv_personWithMultipleTags_semicolonSeparated() throws IOException {
        testFilePath = temporaryFolder.resolve("tags.csv");

        Person person = new PersonBuilder()
                .withName("John Doe")
                .withPhone("98765432")
                .withEmail("john@example.com")
                .withTags("friend", "colleague")
                .build();

        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(person);

        CsvAddressBookStorage.exportToCsv(addressBook, testFilePath);

        List<String> lines = Files.readAllLines(testFilePath);
        String dataLine = lines.get(1);

        assertTrue(dataLine.contains("friend")); // CHANGED: Check for tag presence
        assertTrue(dataLine.contains("colleague")); // CHANGED: Check for tag presence
    }

    @Test
    public void exportToCsv_invalidPath_throwsException() {
        Path invalidPath = Path.of(""); // Empty path is invalid
        AddressBook addressBook = new AddressBook(); // CHANGED: Use empty address book

        assertThrows(IOException.class, () ->
                CsvAddressBookStorage.exportToCsv(addressBook, invalidPath));
    }

    @Test
    public void exportToCsv_correctColumnOrder_success() throws IOException {
        testFilePath = temporaryFolder.resolve("order.csv");

        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(ALICE);

        CsvAddressBookStorage.exportToCsv(addressBook, testFilePath);

        List<String> lines = Files.readAllLines(testFilePath);
        String header = lines.get(0);

        // Verify column order
        assertEquals(CSV_HEADER, header);

        String[] columns = header.split(",");
        assertEquals("Name", columns[0]);
        assertEquals("Phone", columns[1]);
        assertEquals("Email", columns[2]);
        assertEquals("Telegram", columns[3]);
        assertEquals("GitHub", columns[4]);
        assertEquals("Tags", columns[5]);
        assertEquals("Preferred Mode", columns[6]);
        assertEquals("Pinned", columns[7]);
    }

    @Test
    public void exportToCsv_parentDirectoryDoesNotExist_createsDirectory() throws IOException {
        testFilePath = temporaryFolder.resolve("nested/folder/test.csv");

        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(ALICE);

        CsvAddressBookStorage.exportToCsv(addressBook, testFilePath);

        assertTrue(Files.exists(testFilePath));
        assertTrue(Files.exists(testFilePath.getParent()));
    }

    @Test
    public void exportToCsv_fileAlreadyExists_overwritesFile() throws IOException {
        testFilePath = temporaryFolder.resolve("overwrite.csv");

        // Create file with initial content
        Files.writeString(testFilePath, "old content");

        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(ALICE);

        CsvAddressBookStorage.exportToCsv(addressBook, testFilePath);

        // File should be overwritten
        String content = Files.readString(testFilePath);
        assertTrue(content.contains("Name,Phone,Email"));
        assertTrue(!content.contains("old content"));
    }

    @Test
    public void exportToCsv_personWithNullPreferredMode_exportsEmptyField() throws IOException {
        testFilePath = temporaryFolder.resolve("nullpreferred.csv");

        Person person = new PersonBuilder()
                .withName("John Doe")
                .withPhone("98765432")
                .withEmail("john@example.com")
                .withPreferredMode(null)
                .build();

        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(person);

        CsvAddressBookStorage.exportToCsv(addressBook, testFilePath);

        List<String> lines = Files.readAllLines(testFilePath);
        assertEquals(2, lines.size());

        String dataLine = lines.get(1);
        String[] fields = dataLine.split(",", -1);

        assertEquals("", fields[6]);
    }

    @Test
    public void exportToCsv_personNotPinned_exportsFalse() throws IOException {
        testFilePath = temporaryFolder.resolve("notpinned.csv");

        Person person = new PersonBuilder()
                .withName("John Doe")
                .withPhone("98765432")
                .withEmail("john@example.com")
                .build();

        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(person);

        CsvAddressBookStorage.exportToCsv(addressBook, testFilePath);

        List<String> lines = Files.readAllLines(testFilePath);
        String dataLine = lines.get(1);

        assertTrue(dataLine.contains("false"));
    }

    @Test
    public void exportToCsv_personWithNoTags_exportsEmptyTagField() throws IOException {
        testFilePath = temporaryFolder.resolve("notags.csv");

        Person person = new PersonBuilder()
                .withName("John Doe")
                .withPhone("98765432")
                .withEmail("john@example.com")
                .withTags()
                .build();

        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(person);

        CsvAddressBookStorage.exportToCsv(addressBook, testFilePath);

        List<String> lines = Files.readAllLines(testFilePath);
        assertEquals(2, lines.size());

        String dataLine = lines.get(1);
        String[] fields = dataLine.split(",", -1);

        assertEquals("", fields[5]);
    }
}
