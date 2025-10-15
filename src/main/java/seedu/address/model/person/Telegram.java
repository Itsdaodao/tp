package seedu.address.model.person;

import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Objects;

/**
 * Represents a Person's telegram username in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidTelegram(String)}
 */
public class Telegram {

    public static final String MESSAGE_CONSTRAINTS =
            "A Telegram username must be between 5 and 32 characters long and "
                    + "can only contain letters (a-z), numbers (0-9), and underscores.";
    public static final String VALIDATION_REGEX = "^[a-zA-Z0-9_]{5,32}$";
    public final String value;

    /**
     * Constructs an empty {@code Telegram} with null value.
     */
    public Telegram() {
        value = null;
    }

    /**
     * Constructs a {@code Telegram}.
     *
     * @param username A valid telegram username.
     */
    public Telegram(String username) {
        String lowerCaseUsername = username == null ? null : username.toLowerCase();
        checkArgument(isValidTelegram(lowerCaseUsername), MESSAGE_CONSTRAINTS);
        value = lowerCaseUsername;
    }

    /**
     * Returns true if a given string is a valid Telegram username.
     */
    public static boolean isValidTelegram(String test) {
        // Telegram is optional field, Null value is allowed
        if (test == null) {
            return true;
        }

        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns true if telegram value is null value
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
        if (!(other instanceof Telegram)) {
            return false;
        }

        Telegram otherTelegram = (Telegram) other;
        return Objects.equals(value, otherTelegram.value);
    }

    @Override
    public int hashCode() {
        if (value == null) {
            return 0;
        }

        return value.hashCode();
    }
}
