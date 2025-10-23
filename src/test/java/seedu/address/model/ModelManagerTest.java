package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.ELLE;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.PersonBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new AddressBook(), new AddressBook(modelManager.getAddressBook()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(Paths.get("address/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setAddressBookFilePath(Paths.get("new/address/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setAddressBookFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setAddressBookFilePath(null));
    }

    @Test
    public void setAddressBookFilePath_validPath_setsAddressBookFilePath() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setAddressBookFilePath(path);
        assertEquals(path, modelManager.getAddressBookFilePath());
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        modelManager.addPerson(ALICE);
        assertTrue(modelManager.hasPerson(ALICE));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(
                UnsupportedOperationException.class, () -> modelManager.getSortedAndFilteredPersonList().remove(0)
        );
    }

    @Test
    public void equals() {
        AddressBook addressBook = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON).build();
        AddressBook differentAddressBook = new AddressBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(addressBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(addressBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(addressBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAddressBookFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(addressBook, differentUserPrefs)));
    }

    @Test
    public void exportAddressBookToCsv_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.exportAddressBookToCsv(null));
    }

    @Test
    public void exportAddressBookToCsv_validPath_success() throws Exception {
        Path tempFile = Files.createTempFile("test", ".csv");
        try {
            modelManager.addPerson(ALICE);
            modelManager.exportAddressBookToCsv(tempFile);

            assertTrue(Files.exists(tempFile));
            String content = Files.readString(tempFile);
            assertTrue(content.contains("Name,Phone,Email,Telegram,GitHub,Tags"));
            assertTrue(content.contains("Alice"));
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    public void exportAddressBookToCsv_emptyAddressBook_exportsHeaderOnly() throws Exception {
        Path tempFile = Files.createTempFile("test", ".csv");
        try {
            modelManager.exportAddressBookToCsv(tempFile);

            assertTrue(Files.exists(tempFile));
            String content = Files.readString(tempFile);
            String[] lines = content.split("\n");
            assertEquals(1, lines.length); // Only header
            assertTrue(lines[0].contains("Name,Phone,Email"));
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    public void withPinPriority_pinnedBeforeUnpinned_success() {
        // Create test persons inside the test method
        Instant now = Instant.now();

        Person unpinnedElle = new PersonBuilder(ELLE).build();
        Person pinnedBenson = new PersonBuilder(BENSON)
                .withPinnedAt(now.toString()).build();

        Comparator<Person> comparator = getWithPinPriorityComparator(null);

        // Pinned person should come before unpinned person
        assertTrue(comparator.compare(pinnedBenson, unpinnedElle) < 0,
                "Pinned person should come before unpinned person");

        assertTrue(comparator.compare(unpinnedElle, pinnedBenson) > 0,
                "Unpinned person should come after pinned person");
    }

    @Test
    public void withPinPriority_bothPinned_sortByMostRecentFirst() {
        // Create pinned persons with different timestamps
        Instant now = Instant.now();
        Instant oneHourAgo = now.minusSeconds(3600);
        Instant twoDaysAgo = now.minusSeconds(172800);

        Person pinnedBensonOld = new PersonBuilder(BENSON)
                .withPinnedAt(twoDaysAgo.toString()).build();

        Person pinnedCarlMiddle = new PersonBuilder(CARL)
                .withPinnedAt(oneHourAgo.toString()).build();

        Person pinnedElleRecent = new PersonBuilder(ELLE)
                .withPinnedAt(now.toString()).build();

        Comparator<Person> comparator = getWithPinPriorityComparator(null);

        // More recently pinned should come first
        assertTrue(comparator.compare(pinnedElleRecent, pinnedCarlMiddle) < 0,
                "More recently pinned person should come first");

        assertTrue(comparator.compare(pinnedCarlMiddle, pinnedBensonOld) < 0,
                "More recently pinned person should come first");

        assertTrue(comparator.compare(pinnedBensonOld, pinnedElleRecent) > 0,
                "Older pinned person should come after newer pinned person");
    }

    @Test
    public void withPinPriority_bothUnpinnedWithBaseComparator_sortByAlphabetically() {
        // Create unpinned persons
        Person unpinnedBenson = new PersonBuilder(BENSON).build();
        Person unpinnedCarl = new PersonBuilder(CARL).build();
        Person unpinnedElle = new PersonBuilder(ELLE).build();

        // Alphabetical comparator
        Comparator<Person> nameComparator = (p1, p2) ->
                p1.getName().fullName.compareToIgnoreCase(p2.getName().fullName);
        Comparator<Person> comparator = getWithPinPriorityComparator(nameComparator);

        // Unpinned persons should be sorted alphabetically
        assertTrue(comparator.compare(unpinnedBenson, unpinnedCarl) < 0,
                "Benson should come before Carl alphabetically");

        assertTrue(comparator.compare(unpinnedCarl, unpinnedElle) < 0,
                "Carl should come before Elle alphabetically");
    }

    @Test
    public void withPinPriority_bothUnpinnedWithNullBaseComparator_returnsZero() {
        // Create unpinned persons
        Person unpinnedBenson = new PersonBuilder(BENSON).build();
        Person unpinnedElle = new PersonBuilder(ELLE).build();

        Comparator<Person> comparator = getWithPinPriorityComparator(null);

        // With null base comparator, unpinned persons are considered equal
        assertEquals(0, comparator.compare(unpinnedBenson, unpinnedElle),
                "With null base comparator, unpinned persons should be equal");
    }

    @Test
    public void withPinPriority_mixedPinnedAndUnpinned_correctOrder() {
        // Create test persons
        Instant now = Instant.now();
        Instant oneHourAgo = now.minusSeconds(3600);

        Person unpinnedCarl = new PersonBuilder(CARL).build();
        Person pinnedBenson = new PersonBuilder(BENSON)
                .withPinnedAt(oneHourAgo.toString()).build();
        Person pinnedDaniel = new PersonBuilder(DANIEL)
                .withPinnedAt(now.toString()).build();
        Person unpinnedElle = new PersonBuilder(ELLE).build();

        // Add to model
        ModelManager testModel = new ModelManager();
        testModel.addPerson(unpinnedCarl);
        testModel.addPerson(pinnedBenson);
        testModel.addPerson(unpinnedElle);
        testModel.addPerson(pinnedDaniel);

        // Apply name sort
        testModel.applyNameSort();

        var sortedList = testModel.getSortedAndFilteredPersonList();

        // Expected order:
        // 1. Pinned (by pinnedAt, most recent first): Daniel, Benson
        // 2. Unpinned (alphabetically): Carl, Elle
        assertEquals(4, sortedList.size());

        // First two should be pinned
        assertTrue(sortedList.get(0).isPinned(), "First person should be pinned");
        assertTrue(sortedList.get(1).isPinned(), "Second person should be pinned");

        // Last two should be unpinned
        assertFalse(sortedList.get(2).isPinned(), "Third person should be unpinned");
        assertFalse(sortedList.get(3).isPinned(), "Fourth person should be unpinned");

        // Check pinned order (most recent first)
        assertEquals(pinnedDaniel.getName(), sortedList.get(0).getName());
        assertEquals(pinnedBenson.getName(), sortedList.get(1).getName());

        // Check unpinned order (alphabetical)
        assertEquals(unpinnedCarl.getName(), sortedList.get(2).getName());
        assertEquals(unpinnedElle.getName(), sortedList.get(3).getName());
    }

    @SuppressWarnings("unchecked")
    private Comparator<Person> getWithPinPriorityComparator(Comparator<Person> baseComparator) {
        try {
            java.lang.reflect.Method method = ModelManager.class.getDeclaredMethod(
                    "withPinPriority", Comparator.class);
            method.setAccessible(true);
            return (Comparator<Person>) method.invoke(modelManager, baseComparator);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access withPinPriority method", e);
        }
    }
}
