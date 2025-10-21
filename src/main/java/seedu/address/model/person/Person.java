package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.Instant;
import java.util.Collections;
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

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;
    private final Telegram telegram;
    private final Github github;

    // Data fields
    private final Set<Tag> tags = new HashSet<>();

    // Status fields
    private final Boolean isPinned;
    private final Optional<Instant> pinnedAt;

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Telegram telegram, Github github,
                  Set<Tag> tags) {
        requireAllNonNull(name, phone, email, telegram, github, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.telegram = telegram;
        this.github = github;
        this.tags.addAll(tags);
        this.isPinned = false;
        this.pinnedAt = Optional.empty();
    }

    /**
     * Constructs a Person with the isPinned and pinnedAt field.
     * pinnedAt field can be null, every other field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Telegram telegram, Github github,
                  Set<Tag> tags, Boolean isPinned, Instant pinnedAt) {
        requireAllNonNull(name, phone, email, telegram, github, tags, isPinned);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.telegram = telegram;
        this.github = github;
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
        return new Person(name, phone, email, telegram, github, tags, isPinned, pinnedAt);
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
                && tags.equals(otherPerson.tags)
                && isPinned.equals(otherPerson.isPinned)
                && pinnedAt.equals(otherPerson.pinnedAt);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, telegram, github, tags, isPinned, pinnedAt);
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
