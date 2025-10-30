package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Email;
import seedu.address.model.person.Github;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.PreferredCommunicationMode;
import seedu.address.model.person.Telegram;
import seedu.address.model.tag.Tag;

public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_PREFERRED_MODE = "none";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "123456";
    private static final String VALID_EMAIL = "rachel@example.com";
    private static final String VALID_PREFERRED_MODE_1 = "telegram";
    private static final String VALID_PREFERRED_MODE_2 = "phone";
    private static final String VALID_TAG_1 = "friend";
    private static final String VALID_TAG_2 = "neighbour";

    private static final boolean ACCEPT_EMPTY_STRING = true;
    private static final boolean NOT_ACCEPT_EMPTY_STRING = false;

    private static final String WHITESPACE = " \t\r\n";

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, ()
            -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parsePhone_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone((String) null));
    }

    @Test
    public void parsePhone_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(VALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(phoneWithWhitespace));
    }

    @Test
    public void parseEmail_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseEmail((String) null, ACCEPT_EMPTY_STRING));
    }

    @Test
    public void parseEmail_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(INVALID_EMAIL, ACCEPT_EMPTY_STRING));
    }

    @Test
    public void parseEmail_validValueWithoutWhitespace_returnsEmail() throws Exception {
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(VALID_EMAIL, ACCEPT_EMPTY_STRING));
    }

    @Test
    public void parseEmail_validValueWithWhitespace_returnsTrimmedEmail() throws Exception {
        String emailWithWhitespace = WHITESPACE + VALID_EMAIL + WHITESPACE;
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(emailWithWhitespace, ACCEPT_EMPTY_STRING));
    }

    @Test
    public void parseEmail_emptyStringDisallowed_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail("", NOT_ACCEPT_EMPTY_STRING));
    }

    @Test
    public void parseEmail_emptyStringAllowed_returnsEmptyEmail() throws Exception {
        Email expectedEmail = new Email();
        assertEquals(expectedEmail, ParserUtil.parseEmail("", ACCEPT_EMPTY_STRING));
    }

    @Test
    public void parseTelegram_emptyStringDisallowed_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTelegram("", NOT_ACCEPT_EMPTY_STRING));
    }

    @Test
    public void parseTelegram_emptyStringAllowed_returnsEmptyEmail() throws Exception {
        Telegram expectedTelegram = new Telegram();
        assertEquals(expectedTelegram, ParserUtil.parseTelegram("", ACCEPT_EMPTY_STRING));
    }

    @Test
    public void parseGithub_emptyStringDisallowed_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseGithub("", NOT_ACCEPT_EMPTY_STRING));
    }

    @Test
    public void parseGithub_emptyStringAllowed_returnsEmptyGithub() throws Exception {
        Github expectedGithub = new Github();
        assertEquals(expectedGithub, ParserUtil.parseGithub("", ACCEPT_EMPTY_STRING));
    }

    @Test
    public void parsePreferredMode_invalidMode_throwsParseException() {
        Set<PreferredCommunicationMode> availableModes =
                EnumSet.of(PreferredCommunicationMode.PHONE, PreferredCommunicationMode.EMAIL);

        // Invalid mode (not in availableModes)
        assertThrows(ParseException.class, () ->
                ParserUtil.parsePreferredMode(VALID_PREFERRED_MODE_1, availableModes, ACCEPT_EMPTY_STRING));

        // Explicitly disallowed NONE
        assertThrows(ParseException.class, () ->
                ParserUtil.parsePreferredMode(INVALID_PREFERRED_MODE, availableModes, ACCEPT_EMPTY_STRING));
    }

    @Test
    public void parsePreferredMode_emptyStringDisallowed_throwsParseException() {
        Set<PreferredCommunicationMode> availableModes =
                EnumSet.of(PreferredCommunicationMode.PHONE, PreferredCommunicationMode.EMAIL);

        assertThrows(ParseException.class, () -> ParserUtil.parsePreferredMode("", availableModes,
                NOT_ACCEPT_EMPTY_STRING));
    }

    @Test
    public void parsePreferredMode_emptyStringAllowed_returnsPreferredModeNone() throws Exception {
        Set<PreferredCommunicationMode> availableModes =
                EnumSet.of(PreferredCommunicationMode.PHONE, PreferredCommunicationMode.EMAIL);

        PreferredCommunicationMode expectedMode = PreferredCommunicationMode.NONE;
        assertEquals(expectedMode, ParserUtil.parsePreferredMode("", availableModes, ACCEPT_EMPTY_STRING));
    }

    @Test
    public void parsePreferredMode_validMode_success() throws Exception {
        Set<PreferredCommunicationMode> availableModes =
                EnumSet.of(PreferredCommunicationMode.PHONE, PreferredCommunicationMode.EMAIL);

        assertEquals(PreferredCommunicationMode.PHONE,
                ParserUtil.parsePreferredMode(VALID_PREFERRED_MODE_2, availableModes, ACCEPT_EMPTY_STRING));
    }

    @Test
    public void parseTag_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTag(null));
    }

    @Test
    public void parseTag_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTag(INVALID_TAG));
    }

    @Test
    public void parseTag_validValueWithoutWhitespace_returnsTag() throws Exception {
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(VALID_TAG_1));
    }

    @Test
    public void parseTag_validValueWithWhitespace_returnsTrimmedTag() throws Exception {
        String tagWithWhitespace = WHITESPACE + VALID_TAG_1 + WHITESPACE;
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(tagWithWhitespace));
    }

    @Test
    public void parseTags_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTags(null));
    }

    @Test
    public void parseTags_collectionWithInvalidTags_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, INVALID_TAG)));
    }

    @Test
    public void parseTags_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseTags(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseTags_collectionWithValidTags_returnsTagSet() throws Exception {
        Set<Tag> actualTagSet = ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, VALID_TAG_2));
        Set<Tag> expectedTagSet = new HashSet<Tag>(Arrays.asList(new Tag(VALID_TAG_1), new Tag(VALID_TAG_2)));

        assertEquals(expectedTagSet, actualTagSet);
    }
}
