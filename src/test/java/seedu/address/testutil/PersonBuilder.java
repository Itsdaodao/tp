package seedu.address.testutil;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import seedu.address.model.person.Email;
import seedu.address.model.person.Github;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.PinStatus;
import seedu.address.model.person.PreferredCommunicationMode;
import seedu.address.model.person.Telegram;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_TELEGRAM = "amy_bee";
    public static final String DEFAULT_GITHUB = "amy-bee";
    public static final String DEFAULT_PINNEDAT = "2025-10-21T12:49:39.699362800Z";
    public static final String DEFAULT_PREFERRED_MODE = "telegram";


    private Name name;
    private Phone phone;
    private Email email;
    private Telegram telegram;
    private Github github;
    private PreferredCommunicationMode preferredMode;
    private Set<Tag> tags;
    private Boolean isPinned;
    private Instant pinnedAt;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email();
        telegram = new Telegram();
        github = new Github();
        preferredMode = PreferredCommunicationMode.of(null);
        tags = new HashSet<>();
        isPinned = false;
        pinnedAt = null;
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        telegram = personToCopy.getTelegram();
        github = personToCopy.getGithub();
        preferredMode = personToCopy.getPreferredMode();
        tags = new HashSet<>(personToCopy.getTags());
        isPinned = personToCopy.isPinned();
        pinnedAt = personToCopy.getPinnedAt().orElse(null);
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets a default {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail() {
        this.email = new Email(DEFAULT_EMAIL);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets a default {@code Telegram} of the {@code Person} that we are building.
     */
    public PersonBuilder withTelegram() {
        this.telegram = new Telegram(DEFAULT_TELEGRAM);
        return this;
    }

    /**
     * Sets the {@code Telegram} of the {@code Person} that we are building.
     */
    public PersonBuilder withTelegram(String username) {
        this.telegram = new Telegram(username);
        return this;
    }

    /**
     * Sets a default {@code Github} of the {@code Person} that we are building.
     */
    public PersonBuilder withGithub() {
        this.github = new Github(DEFAULT_GITHUB);
        return this;
    }

    /**
     * Sets the {@code Github} of the {@code Person} that we are building.
     */
    public PersonBuilder withGithub(String username) {
        this.github = new Github(username);
        return this;
    }

    /**
     * Sets a default {@code PreferredCommunicationMode} of the {@code Person} that we are building.
     */
    public PersonBuilder withPreferredMode() {
        this.preferredMode = PreferredCommunicationMode.of(DEFAULT_PREFERRED_MODE);
        return this;
    }

    /**
     * Sets the {@code PreferredCommunicationMode} of the {@code Person} that we are building.
     */
    public PersonBuilder withPreferredMode(String username) {
        this.preferredMode = PreferredCommunicationMode.of(username);
        return this;
    }

    /**
     * Sets a default {@code pinnedAt} of the {@code Person} that we are building.
     */
    public PersonBuilder withPinnedAt() {
        this.isPinned = true;
        this.pinnedAt = Instant.parse(DEFAULT_PINNEDAT);
        return this;
    }

    /**
     * Sets the {@code pinnedAt} of the {@code Person} that we are building.
     */
    public PersonBuilder withPinnedAt(String pinnedAt) {
        this.isPinned = true;
        try {
            this.pinnedAt = Instant.parse(pinnedAt);
        } catch (Exception e) {
            this.isPinned = false;
            this.pinnedAt = null;

            throw new IllegalArgumentException(PinStatus.PIN_DATE_MESSAGE_CONSTRAINT);
        }
        return this;
    }

    public Person build() {
        return new Person(name, phone, email, telegram, github, preferredMode, tags, isPinned, pinnedAt);
    }

}
