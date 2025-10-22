package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.BENSON_WITHOUT_EMAIL;
import static seedu.address.testutil.TypicalPersons.BENSON_WITHOUT_GITHUB;
import static seedu.address.testutil.TypicalPersons.BENSON_WITHOUT_PREFERRED_MODE;
import static seedu.address.testutil.TypicalPersons.BENSON_WITHOUT_TELEGRAM;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Email;
import seedu.address.model.person.Github;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.PreferredCommunicationMode;
import seedu.address.model.person.Telegram;

public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TELEGRAM = "john doe";
    private static final String INVALID_GITHUB = "john--doe";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_PREFERRED_MODE = "noo";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_TELEGRAM = BENSON.getTelegram().toString();
    private static final String VALID_GITHUB = BENSON.getGithub().toString();
    private static final String VALID_PREFERRED_MODE = BENSON.getPreferredMode().toString();
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_validPersonWithNullEmail_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_PHONE, null, VALID_TELEGRAM, VALID_GITHUB, VALID_PREFERRED_MODE, VALID_TAGS);
        assertEquals(BENSON_WITHOUT_EMAIL, person.toModelType());
    }

    @Test
    public void toModelType_validPersonWithNullTelegram_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_PHONE, VALID_EMAIL, null, VALID_GITHUB, VALID_PREFERRED_MODE, VALID_TAGS);
        assertEquals(BENSON_WITHOUT_TELEGRAM, person.toModelType());
    }

    @Test
    public void toModelType_validPersonWithNullGithub_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_TELEGRAM, null, VALID_PREFERRED_MODE, VALID_TAGS);
        assertEquals(BENSON_WITHOUT_GITHUB, person.toModelType());
    }

    @Test
    public void toModelType_validPersonWithNullPreferredMode_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_TELEGRAM, VALID_GITHUB, null, VALID_TAGS);
        assertEquals(BENSON_WITHOUT_PREFERRED_MODE, person.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(
                        INVALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_TELEGRAM, VALID_GITHUB, VALID_PREFERRED_MODE,
                        VALID_TAGS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                null, VALID_PHONE, VALID_EMAIL, VALID_TELEGRAM, VALID_GITHUB, VALID_PREFERRED_MODE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(
                        VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_TELEGRAM, VALID_GITHUB, VALID_PREFERRED_MODE,
                        VALID_TAGS);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, null, VALID_EMAIL, VALID_TELEGRAM, VALID_GITHUB, VALID_PREFERRED_MODE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(
                        VALID_NAME, VALID_PHONE, INVALID_EMAIL, VALID_TELEGRAM, VALID_GITHUB, VALID_PREFERRED_MODE,
                        VALID_TAGS);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTelegram_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(
                        VALID_NAME, VALID_PHONE, VALID_EMAIL, INVALID_TELEGRAM, VALID_GITHUB, VALID_PREFERRED_MODE,
                        VALID_TAGS);
        String expectedMessage = Telegram.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidGithub_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(
                        VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_TELEGRAM, INVALID_GITHUB, VALID_PREFERRED_MODE,
                        VALID_TAGS);
        String expectedMessage = Github.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPreferredMode_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(
                        VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_TELEGRAM, VALID_GITHUB, INVALID_PREFERRED_MODE,
                        VALID_TAGS);
        String expectedMessage = PreferredCommunicationMode.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(
                        VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_TELEGRAM, VALID_GITHUB, VALID_PREFERRED_MODE,
                        invalidTags);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

}
