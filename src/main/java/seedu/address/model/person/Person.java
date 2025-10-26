package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.Instant;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    public static final String PIN_DATE_MESSAGE_CONSTRAINT =
            "Invalid date time format. \n"
                    + "pinnedAt must follow the example date time format below \n"
                    + "Format: <yyyy-MM-dd>T<hr:mm:ss>Z \n"
                    + "Example: 2025-10-22T07:00:17.036469800Z";

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;
    private final Telegram telegram;
    private final Github github;
    private final PreferredCommunicationMode preferredMode;

    // Data fields
    private final Set<Tag> tags = new HashSet<>();

    // Status fields
    private final Boolean isPinned;
    private final Optional<Instant> pinnedAt;

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Telegram telegram, Github github,
                  PreferredCommunicationMode preferredMode, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, telegram, github, preferredMode, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.telegram = telegram;
        this.github = github;
        this.preferredMode = preferredMode;
        this.tags.addAll(tags);
        this.isPinned = false;
        this.pinnedAt = Optional.empty();
    }

    /**
     * Constructs a Person with the isPinned and pinnedAt field.
     * pinnedAt field can be null, every other field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Telegram telegram, Github github,
                  PreferredCommunicationMode preferredMode,
                  Set<Tag> tags, Boolean isPinned, Instant pinnedAt) {
        requireAllNonNull(name, phone, email, telegram, github, tags, isPinned);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.telegram = telegram;
        this.github = github;
        this.preferredMode = preferredMode;
        this.tags.addAll(tags);
        this.isPinned = isPinned;
        this.pinnedAt = Optional.ofNullable(pinnedAt);
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Telegram getTelegram() {
        return telegram;
    }

    public Github getGithub() {
        return github;
    }

    public PreferredCommunicationMode getPreferredMode() {
        return preferredMode;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Determines which communication modes are available for this person
     * <p>
     * Phone mode is always included as it is compulsory. Optional modes such as email, telegram and github are
     * included only if their corresponding fields are not empty.
     *
     * @return a set of available {@code PreferredCommunicationMode} values
     */
    public Set<PreferredCommunicationMode> getAvailableModes() {
        Set<PreferredCommunicationMode> availableModes = EnumSet.noneOf(PreferredCommunicationMode.class);

        // Compulsory field
        availableModes.add(PreferredCommunicationMode.PHONE);

        // Optional field
        if (!email.isEmpty()) {
            availableModes.add(PreferredCommunicationMode.EMAIL);
        }
        if (!telegram.isEmpty()) {
            availableModes.add(PreferredCommunicationMode.TELEGRAM);
        }
        if (!github.isEmpty()) {
            availableModes.add(PreferredCommunicationMode.GITHUB);
        }

        return availableModes;
    }

    /**
     * Returns a pinned copy of this person, setting the pin timestamp if not already pinned.
     * If the person is already pinned, returns this instance unchanged.
     *
     * @return a new Person instance marked as pinned with the current timestamp if not already pinned
     */
    public Person pin() {
        if (isPinned) {
            return this;
        }

        Boolean isPinned = true;
        Instant pinnedAt = Instant.now();
        return new Person(name, phone, email, telegram, github, preferredMode, tags, isPinned, pinnedAt);
    }

    /**
     * Returns an unpinned copy of this person, removing the pin timestamp if already pinned.
     * If the person is not yet pinned, returns this instance unchanged.
     *
     * @return a new Person instance marked as unpinned with the timestamp removed if already pinned
     */
    public Person unpin() {
        if (!isPinned) {
            return this;
        }

        return new Person(name, phone, email, telegram, github, preferredMode, tags);
    }

    /**
     * Returns true if person isPinned
     */
    public Boolean isPinned() {
        return isPinned;
    }

    /**
     * Returns the time at which person is pinned
     */
    public Optional<Instant> getPinnedAt() {
        return pinnedAt;
    }

    /**
     * Returns true if both persons have the same identity, data and status fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && telegram.equals(otherPerson.telegram)
                && github.equals(otherPerson.github)
                && preferredMode.equals(otherPerson.preferredMode)
                && tags.equals(otherPerson.tags)
                && isPinned.equals(otherPerson.isPinned)
                && pinnedAt.equals(otherPerson.pinnedAt);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, telegram, github, preferredMode, tags, isPinned, pinnedAt);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("telegram", telegram)
                .add("github", github)
                .add("tags", tags)
                .toString();
    }

}
