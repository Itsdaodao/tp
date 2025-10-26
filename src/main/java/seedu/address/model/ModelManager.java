package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final CommandHistory commandHistory;
    private final FilteredList<Person> filteredPersons;
    private final SortedList<Person> sortedPersons;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs, ReadOnlyCommandHistory cmh) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        this.commandHistory = new CommandHistory(cmh);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        sortedPersons = new SortedList<>(filteredPersons);
        sortedPersons.setComparator(withPinPriority(null));
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs(), new CommandHistory());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        addressBook.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        addressBook.setPerson(target, editedPerson);
    }

    //=========== Command History ======================================================================
    @Override
    public void addCommandToHistory(String command) {
        requireNonNull(command);
        commandHistory.addCommandToHistory(command);
    }

    @Override
    public ReadOnlyCommandHistory getCommandHistory() {
        return commandHistory;
    }

    //=========== Filtered/Sorted Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook} which is filtered and optionally sorted.
     */
    @Override
    public ObservableList<Person> getSortedAndFilteredPersonList() {
        return sortedPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public void applyNameSort() {
        Comparator<Person> nameComparator = (p1, p2) ->
                p1.getName().fullName.compareToIgnoreCase(p2.getName().fullName);
        sortedPersons.setComparator(withPinPriority(nameComparator));
    }

    @Override
    public void applyRecentSort() {
        Comparator<Person> recentComparator = (p1, p2) -> {
            // Reverse the original list order: the later element in addressBook list appears first
            List<Person> originalList = addressBook.getPersonList();
            int index1 = originalList.indexOf(p1);
            int index2 = originalList.indexOf(p2);
            return Integer.compare(index2, index1); // flip the order
        };
        sortedPersons.setComparator(withPinPriority(recentComparator));
    }

    @Override
    public void resetSortOrder() {
        sortedPersons.setComparator(withPinPriority(null));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons)
                && commandHistory.equals(otherModelManager.commandHistory);
    }

    /**
     * Creates a comparator that ensures pinned contacts appear first.
     * Within pinned contacts, they are sorted by pinnedAt (most recent first).
     * Within unpinned contacts, they follow the provided base comparator.
     */
    private Comparator<Person> withPinPriority(Comparator<Person> baseComparator) {
        return (p1, p2) -> {
            // If pin status differs, pinned person comes first
            if (p1.isPinned() != p2.isPinned()) {
                return p1.isPinned() ? -1 : 1;
            }

            // Both pinned, sort by pinnedAt timestamp (most recent first)
            if (p1.isPinned() && p2.isPinned()) {
                if (p1.getPinnedAt().isPresent() && p2.getPinnedAt().isPresent()) {
                    // Reverse order: more recent pinnedAt comes first
                    return p2.getPinnedAt().get().compareTo(p1.getPinnedAt().get());
                } else if (p1.getPinnedAt().isPresent()) {
                    return -1;
                } else if (p2.getPinnedAt().isPresent()) {
                    return 1;
                }
                // Both missing pinnedAt, fall through to base comparator
            }

            // Neither are pinned, use base comparator
            if (baseComparator != null) {
                return baseComparator.compare(p1, p2);
            }
            return 0;
        };
    }

}
