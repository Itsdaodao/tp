package seedu.address.model.person;

import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a Person's preferred mode of communication in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidMode(String)}.
 */
public class PreferredCommunicationMode {

    public static final String MESSAGE_CONSTRAINTS =
            "Preferred mode should be one of: phone, email, telegram or github.";

    public static final String[] VALID_MODES = {"phone", "email", "telegram", "github"};
    public final String value;

    /**
     * Constructs an empty {@code PreferredCommunicationMode} with null value.
     */
    public PreferredCommunicationMode() {
        value = null;
    }

    /**
     * Constructs a {@code PreferredCommunicationMode}.
     *
     * @param mode A valid communication mode.
     */
    public PreferredCommunicationMode(String mode) {
        String lowerCaseMode = mode == null ? null : mode.toLowerCase();
        checkArgument(isValidMode(lowerCaseMode), MESSAGE_CONSTRAINTS);
        this.value = lowerCaseMode;
    }

    /**
     * Returns true if a given string is a valid communication mode.
     */
    public static boolean isValidMode(String test) {
        // Preferred communication mode is optional field, Null value is allowed
        boolean isNull = test == null;
        boolean matchesValidMode = Arrays.stream(VALID_MODES)
                .anyMatch(validMode -> validMode.equalsIgnoreCase(test));

        boolean isValid = isNull || matchesValidMode;
        return isValid;
    }

    /**
     * Returns true if preferredCommunicationMode is null value
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
        if (!(other instanceof PreferredCommunicationMode)) {
            return false;
        }

        PreferredCommunicationMode otherMode = (PreferredCommunicationMode) other;
        return Objects.equals(value, otherMode.value);
    }

    @Override
    public int hashCode() {
        if (value == null) {
            return 0;
        }

        return value.hashCode();
    }
}
