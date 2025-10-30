package seedu.address.model.person;

import java.util.Arrays;
import java.util.Set;

/**
 * Represents a Person's preferred mode of communication in the address book.
 */
public enum PreferredCommunicationMode {
    PHONE,
    EMAIL,
    TELEGRAM,
    NONE; // Represents empty/null preferred modes

    public static final String MESSAGE_CONSTRAINTS =
            "Preferred mode should be one of: phone, email or telegram.";
    public static final String MESSAGE_INVALID_PREFERRED_MODE =
            "Invalid preferred mode: %s. The person does not have this contact method.";

    /**
     * Validates whether the given input string is a valid preferred communication mode.
     * <p>
     * - If {@code availableModes} is {@code null}, all enum values are considered valid.
     * - If {@code allowNone} is {@code true}, the string "NONE" is always accepted.
     * - Otherwise, the input must match one of the provided {@code availableModes}, excluding NONE.
     *
     * @param test the input string to validate
     * @param availableModes the set of modes available for the current context; may be {@code null}
     * @param allowNone whether the mode "NONE" should be considered valid
     * @return true if the input is valid under the given conditions
     */
    public static boolean isValidMode(String test, Set<PreferredCommunicationMode> availableModes, boolean allowNone) {
        boolean isNull = test == null || test.isBlank();
        boolean isExplicitNone = !isNull && test.equalsIgnoreCase(PreferredCommunicationMode.NONE.name());
        boolean isValidMode;

        if (isNull) {
            return true;
        }
        // Case 1: No restriction, must match any defined enum
        if (availableModes == null) {
            boolean matchesAnyEnum = Arrays.stream(PreferredCommunicationMode.values())
                    .anyMatch(mode -> mode.name().equalsIgnoreCase(test));
            isValidMode = matchesAnyEnum;
        } else {
            // Case 2: NONE is explicitly allowed
            boolean isAllowedNone = allowNone && isExplicitNone;

            // Case 3: Must match one of the available modes (excluding NONE)
            boolean matchesAvailableMode = availableModes.stream()
                    .anyMatch(mode -> mode.name().equalsIgnoreCase(test) && mode != NONE);
            isValidMode = isAllowedNone || matchesAvailableMode;
        }

        return isValidMode;
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

        // Find and return a matching enum, default to NONE if no match
        PreferredCommunicationMode matchedMode = Arrays.stream(PreferredCommunicationMode.values())
                .filter(mode -> mode != PreferredCommunicationMode.NONE)
                .filter(mode -> mode.name().equalsIgnoreCase(trimmedMode))
                .findFirst()
                .orElse(PreferredCommunicationMode.NONE);

        return matchedMode;
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
