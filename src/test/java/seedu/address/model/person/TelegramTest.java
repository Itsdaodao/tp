package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Objects;

import org.junit.jupiter.api.Test;

public class TelegramTest {

    @Test
    public void constructor_invalidTelegram_throwsIllegalArgumentException() {
        String invalidUsername = "";
        assertThrows(IllegalArgumentException.class, () -> new Telegram(invalidUsername));
    }

    @Test
    public void isValidTelegram() {
        // blank Telegram
        assertFalse(Telegram.isValidTelegram("")); // empty string
        assertFalse(Telegram.isValidTelegram(" ")); // spaces only

        // invalid length
        assertFalse(Telegram.isValidTelegram("abcd")); // too short (4 chars)
        assertFalse(Telegram.isValidTelegram("a".repeat(33))); // too long (33 chars)

        // invalid characters
        assertFalse(Telegram.isValidTelegram("user name")); // space
        assertFalse(Telegram.isValidTelegram("user-name")); // hyphen
        assertFalse(Telegram.isValidTelegram("user.name")); // period
        assertFalse(Telegram.isValidTelegram("user+name")); // plus
        assertFalse(Telegram.isValidTelegram("user@name")); // at symbol
        assertFalse(Telegram.isValidTelegram("user/name")); // slash
        assertFalse(Telegram.isValidTelegram("username!")); // special char
        assertFalse(Telegram.isValidTelegram("username$")); // special char
        assertFalse(Telegram.isValidTelegram("user123*")); // asterisk
        assertFalse(Telegram.isValidTelegram("12345!")); // ends with invalid char

        // valid Telegram
        assertTrue(Telegram.isValidTelegram(null)); // empty Telegram
        assertTrue(Telegram.isValidTelegram("abcde")); // min length (5)
        assertTrue(Telegram.isValidTelegram("abcde1")); // 6 chars
        assertTrue(Telegram.isValidTelegram("a1b2c")); // mix of letters and numbers
        assertTrue(Telegram.isValidTelegram("user_123")); // underscore allowed
        assertTrue(Telegram.isValidTelegram("User_Name")); // uppercase allowed
        assertTrue(Telegram.isValidTelegram("a".repeat(32))); // max length (32)
        assertTrue(Telegram.isValidTelegram("test123456789012345678901234567")); // exactly 32 chars
    }

    @Test
    public void isEmpty() {
        Telegram telegram = new Telegram();
        assertTrue(telegram.isEmpty());
    }

    @Test
    public void equals() {
        Telegram telegram = new Telegram("valid_username");
        Telegram emptyTelegram = new Telegram();

        // same values -> returns true
        assertTrue(telegram.equals(new Telegram("valid_username")));

        // same null values -> returns true
        assertTrue(emptyTelegram.equals(new Telegram(null)));

        // same object -> returns true
        assertTrue(telegram.equals(telegram));

        // null -> returns false
        assertFalse(telegram.equals(null));

        // different types -> returns false
        assertFalse(telegram.equals(5.0f));

        // different values -> returns false
        assertFalse(telegram.equals(new Telegram("other_username")));
    }

    @Test
    public void toString_null_returnsEmptyString() {
        Telegram telegram = new Telegram();
        assert(Objects.equals(telegram.toString(), ""));
    }
}
