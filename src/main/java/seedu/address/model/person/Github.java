package seedu.address.model.person;

import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Objects;

/**
 * Represents a Person's GitHub username in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidGithub(String)}
 */
public class Github {

    public static final String MESSAGE_CONSTRAINTS =
            "A GitHub username must be between 1 and 39 characters long and can only "
                    + "contain letters (a–z, A–Z), numbers (0–9), and hyphens (-).\n"
                    + "It cannot begin or end with a hyphen, and consecutive hyphens are not allowed.";
    public static final String VALIDATION_REGEX = "^(?!-)(?!.*--)[a-zA-Z0-9-]{1,39}(?<!-)$";
    public final String value;

    /**
     * Constructs an empty {@code Github} with null value.
     */
    public Github() {
        value = null;
    }

    /**
     * Constructs a {@code Github}.
     *
     * @param username A valid GitHub username.
     */
    public Github(String username) {
        String lowerCaseUsername = username == null ? null : username.toLowerCase();
        checkArgument(isValidGithub(lowerCaseUsername), MESSAGE_CONSTRAINTS);
        value = lowerCaseUsername;
    }

    /**
     * Returns true if a given string is a valid GitHub username.
     */
    public static boolean isValidGithub(String test) {
        // GitHub is optional field, null value is allowed
        if (test == null) {
            return true;
        }

        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns true if GitHub value is null value
     */
    public boolean isEmpty() {
        return value == null;
    }

    @Override
    public String toString() {
        if (value == null) {
            return "";
        }
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Github)) {
            return false;
        }

        Github otherGithub = (Github) other;
        return Objects.equals(value, otherGithub.value);
    }

    @Override
    public int hashCode() {
        if (value == null) {
            return 0;
        }

        return value.hashCode();
    }
}
