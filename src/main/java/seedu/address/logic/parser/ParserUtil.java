package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Email;
import seedu.address.model.person.Github;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.PreferredCommunicationMode;
import seedu.address.model.person.Telegram;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    private static final Logger logger = LogsCenter.getLogger(ParserUtil.class);
    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        String nameWithoutAdditionalSpace = trimmedName.replaceAll("\\s+", " ");
        if (!Name.isValidName(nameWithoutAdditionalSpace)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(nameWithoutAdditionalSpace);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email, boolean isAcceptEmptyString) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();

        // Allow empty string if edit mode (for clearing email)
        if (isAcceptEmptyString && trimmedEmail.isEmpty()) {
            return new Email();
        }

        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String username} into a {@code Telegram}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code Telegram} is invalid.
     */
    public static Telegram parseTelegram(String username, boolean isAcceptEmptyString) throws ParseException {
        requireNonNull(username);
        String trimmedUsername = username.trim();

        // Allow empty string if edit mode (for clearing telegram)
        if (isAcceptEmptyString && trimmedUsername.isEmpty()) {
            return new Telegram();
        }

        if (!Telegram.isValidTelegram(trimmedUsername)) {
            throw new ParseException(Telegram.MESSAGE_CONSTRAINTS);
        }
        return new Telegram(trimmedUsername);
    }

    /**
     * Parses a {@code String username} into a {@code Github}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code Github} is invalid.
     */
    public static Github parseGithub(String username, boolean isAcceptEmptyString) throws ParseException {
        requireNonNull(username);
        String trimmedUsername = username.trim();

        // Allow empty string if edit mode (for clearing github)
        if (isAcceptEmptyString && trimmedUsername.isEmpty()) {
            return new Github();
        }

        if (!Github.isValidGithub(trimmedUsername)) {
            throw new ParseException(Github.MESSAGE_CONSTRAINTS);
        }
        return new Github(trimmedUsername);
    }

    /**
     * Parses a {@code String preferredMode} into a {@code PreferredCommunicationMode} enum value.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code PreferredCommunicationMode} is invalid.
     */
    public static PreferredCommunicationMode parsePreferredMode(
            String preferredMode, Set<PreferredCommunicationMode> availableModes, boolean isAcceptEmptyString)
            throws ParseException {
        // available mode can be null
        requireNonNull(preferredMode);
        String trimmedMode = preferredMode.trim();
        boolean allowNone = false;

        // Allow empty string if edit mode (for clearing preferred mode of communication)
        if (isAcceptEmptyString && trimmedMode.isEmpty()) {
            return PreferredCommunicationMode.NONE;
        }

        // Check if the mode is a valid enum value (excluding NONE)
        boolean isRecognized = Arrays.stream(PreferredCommunicationMode.values())
                .anyMatch(mode -> mode.name().equalsIgnoreCase(trimmedMode)
                        && mode != PreferredCommunicationMode.NONE);

        if (!isRecognized) {
            logger.warning("Unrecognized preferred mode:" + trimmedMode);
            throw new ParseException(PreferredCommunicationMode.MESSAGE_CONSTRAINTS);
        }

        if (!PreferredCommunicationMode.isValidMode(trimmedMode, availableModes, allowNone)) {
            logger.warning("Invalid preferred mode: " + trimmedMode);
            throw new ParseException(String.format(
                    PreferredCommunicationMode.MESSAGE_INVALID_PREFERRED_MODE, preferredMode));
        }

        // Find matching enum value
        PreferredCommunicationMode matchedMode = Arrays.stream(PreferredCommunicationMode.values())
                .filter(mode -> mode != PreferredCommunicationMode.NONE)
                .filter(mode -> mode.name().equalsIgnoreCase(trimmedMode))
                .findFirst()
                .orElse(PreferredCommunicationMode.NONE);
        logger.fine(() -> "Successfully parsed preferred mode: " + matchedMode);

        return matchedMode;
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }
}
