package seedu.address.model.person;

import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Arrays;
import java.util.Set;

/**
 * Represents a Person's preferred mode of communication in the address book.
 */
public enum PreferredCommunicationMode {
    PHONE,
    EMAIL,
    TELEGRAM,
    GITHUB,
    NONE; // Represents empty/null preferred modes

    public static final String MESSAGE_CONSTRAINTS =
            "Preferred mode should be one of: phone, email, telegram or github.";
    public static final String MESSAGE_INVALID_PREFERRED_MODE =
            "Invalid preferred mode: %s. The person does not have this contact method.";

    /**
     * Returns true if the input string is a valid preferred mode among the available modes.
     *
     * @param test the input string
     * @param availableModes the set of modes the user actually has
     */
    public static boolean isValidMode(String test, Set<PreferredCommunicationMode> availableModes) {
        // Preferred communication mode is optional field, Null value is allowed
//        boolean isNull = test == null || test.isBlank();
//        if (isNull) {
//            return true;
//        }

//        // Allow NONE explicitly
//        if (PreferredCommunicationMode.NONE.name().equalsIgnoreCase(test)) {
//            return true;
//        }

        // Only allow modes that exist in availableModes
        boolean matchesValidMode = availableModes.stream()
                .anyMatch(mode -> mode.name().equalsIgnoreCase(test) && mode != NONE);

//        boolean isValid = isNull || matchesValidMode;
        return matchesValidMode;
    }

    /**
     * Returns true if a given string is a valid communication mode.
     */
    public static boolean isValidMode(String test) {
        // Preferred communication mode is optional field, Null value is allowed
        boolean isNull = test == null || test.isBlank();
        if (isNull) {
            return true;
        }

        // Allow NONE explicitly
        if (PreferredCommunicationMode.NONE.name().equalsIgnoreCase(test)) {
            return true;
        }

        boolean matchesValidMode = Arrays.stream(values())
//                .filter(mode -> mode != NONE)
                .anyMatch(mode -> mode.name().equalsIgnoreCase(test));

        boolean isValid = isNull || matchesValidMode;
        return isValid;
    }

    /**
     * Converts a {@code String preferredMode} into a {@code PreferredCommunicationMode} enum value.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @param preferredMode string representation of preferred mode
     * @return the corresponding {@code PreferredCommunicationMode}, or {@code NONE} if blank
     */
    public static PreferredCommunicationMode of(String preferredMode) {
        // Handle null or blank inputs safely
        boolean isNullOrBlank = preferredMode == null || preferredMode.trim().isEmpty();
        if (isNullOrBlank) {
            return PreferredCommunicationMode.NONE;
        }

        String trimmedMode = preferredMode.trim();

        // Return the corresponding enum
        return Arrays.stream(PreferredCommunicationMode.values())
                .filter(mode -> mode != PreferredCommunicationMode.NONE)
                .filter(mode -> mode.name().equalsIgnoreCase(trimmedMode))
                .findFirst()
                .orElse(PreferredCommunicationMode.NONE);
    }

    /**
     * Check if this mode is {@code NONE}
     *
     * @return {@code true} if mode is {@code NONE}
     */
    public boolean isEmpty() {
        boolean isEmpty = this == NONE;
        return isEmpty;
    }

    @Override
    public String toString() {
        boolean hasValue = this != NONE;
        String modeString = hasValue ? name().toLowerCase() : "";
        return modeString;
    }

}
