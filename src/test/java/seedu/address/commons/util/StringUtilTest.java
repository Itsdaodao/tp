package seedu.address.commons.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

public class StringUtilTest {
    //---------------- Tests for isNonZeroUnsignedInteger --------------------------------------

    @Test
    public void isNonZeroUnsignedInteger() {

        // EP: empty strings
        assertFalse(StringUtil.isNonZeroUnsignedInteger("")); // Boundary value
        assertFalse(StringUtil.isNonZeroUnsignedInteger("  "));

        // EP: not a number
        assertFalse(StringUtil.isNonZeroUnsignedInteger("a"));
        assertFalse(StringUtil.isNonZeroUnsignedInteger("aaa"));

        // EP: zero
        assertFalse(StringUtil.isNonZeroUnsignedInteger("0"));

        // EP: zero as prefix
        assertTrue(StringUtil.isNonZeroUnsignedInteger("01"));

        // EP: signed numbers
        assertFalse(StringUtil.isNonZeroUnsignedInteger("-1"));
        assertFalse(StringUtil.isNonZeroUnsignedInteger("+1"));

        // EP: numbers with white space
        assertFalse(StringUtil.isNonZeroUnsignedInteger(" 10 ")); // Leading/trailing spaces
        assertFalse(StringUtil.isNonZeroUnsignedInteger("1 0")); // Spaces in the middle

        // EP: number larger than Integer.MAX_VALUE
        assertFalse(StringUtil.isNonZeroUnsignedInteger(Long.toString(Integer.MAX_VALUE + 1)));

        // EP: valid numbers, should return true
        assertTrue(StringUtil.isNonZeroUnsignedInteger("1")); // Boundary value
        assertTrue(StringUtil.isNonZeroUnsignedInteger("10"));
    }


    //---------------- Tests for containsWordIgnoreCase --------------------------------------

    /*
     * Invalid equivalence partitions for word: null, empty, multiple words
     * Invalid equivalence partitions for sentence: null
     * The four test cases below test one invalid input at a time.
     */

    @Test
    public void containsWordIgnoreCase_nullWord_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.containsWordIgnoreCase("typical sentence", null));
    }

    @Test
    public void containsWordIgnoreCase_emptyWord_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, StringUtil.ERROR_EMPTY_KEYWORD, ()
            -> StringUtil.containsWordIgnoreCase("typical sentence", "  "));
    }

    @Test
    public void containsWordIgnoreCase_multipleWords_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, StringUtil.ERROR_MULTIPLE_WORDS, ()
            -> StringUtil.containsWordIgnoreCase("typical sentence", "aaa BBB"));
    }

    @Test
    public void containsWordIgnoreCase_nullSentence_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.containsWordIgnoreCase(null, "abc"));
    }

    /*
     * Valid equivalence partitions for word:
     *   - any word
     *   - word containing symbols/numbers
     *   - word with leading/trailing spaces
     *
     * Valid equivalence partitions for sentence:
     *   - empty string
     *   - one word
     *   - multiple words
     *   - sentence with extra spaces
     *
     * Possible scenarios returning true:
     *   - matches first word in sentence
     *   - last word in sentence
     *   - middle word in sentence
     *   - matches multiple words
     *
     * Possible scenarios returning false:
     *   - query word matches part of a sentence word
     *   - sentence word matches part of the query word
     *
     * The test method below tries to verify all above with a reasonably low number of test cases.
     */

    @Test
    public void containsWordIgnoreCase_validInputs_correctResult() {

        // Empty sentence
        assertFalse(StringUtil.containsWordIgnoreCase("", "abc")); // Boundary case
        assertFalse(StringUtil.containsWordIgnoreCase("    ", "123"));

        // Matches a partial word only
        assertFalse(StringUtil.containsWordIgnoreCase("aaa bbb ccc", "bb")); // Sentence word bigger than query word
        assertFalse(StringUtil.containsWordIgnoreCase("aaa bbb ccc", "bbbb")); // Query word bigger than sentence word

        // Matches word in the sentence, different upper/lower case letters
        assertTrue(StringUtil.containsWordIgnoreCase("aaa bBb ccc", "Bbb")); // First word (boundary case)
        assertTrue(StringUtil.containsWordIgnoreCase("aaa bBb ccc@1", "CCc@1")); // Last word (boundary case)
        assertTrue(StringUtil.containsWordIgnoreCase("  AAA   bBb   ccc  ", "aaa")); // Sentence has extra spaces
        assertTrue(StringUtil.containsWordIgnoreCase("Aaa", "aaa")); // Only one word in sentence (boundary case)
        assertTrue(StringUtil.containsWordIgnoreCase("aaa bbb ccc", "  ccc  ")); // Leading/trailing spaces

        // Matches multiple words in sentence
        assertTrue(StringUtil.containsWordIgnoreCase("AAA bBb ccc  bbb", "bbB"));
    }

    //---------------- Tests for getDetails --------------------------------------

    /*
     * Equivalence Partitions: null, valid throwable object
     */

    @Test
    public void getDetails_exceptionGiven() {
        assertTrue(StringUtil.getDetails(new FileNotFoundException("file not found"))
            .contains("java.io.FileNotFoundException: file not found"));
    }

    @Test
    public void getDetails_nullGiven_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.getDetails(null));
    }

    //---------------- Tests for hasWordStartingWithIgnoreCase --------------------------------------

    /*
     * Invalid equivalence partitions for word: null, empty, multiple words
     * Invalid equivalence partitions for sentence: null
     * The four test cases below test one invalid input at a time.
     */

    @Test
    public void hasWordStartingWithIgnoreCase_nullWord_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.hasWordStartingWithIgnoreCase(
                "typical sentence", null));
    }

    @Test
    public void hasWordStartingWithIgnoreCase_emptyWord_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, StringUtil.ERROR_EMPTY_KEYWORD, ()
                -> StringUtil.hasWordStartingWithIgnoreCase("typical sentence", "  "));
    }

    @Test
    public void hasWordStartingWithIgnoreCase_multipleWords_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, StringUtil.ERROR_MULTIPLE_WORDS, ()
                -> StringUtil.hasWordStartingWithIgnoreCase("typical sentence", "aaa BBB"));
    }

    @Test
    public void hasWordStartingWithIgnoreCase_nullSentence_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.hasWordStartingWithIgnoreCase(null, "abc"));
    }

    /*
     * Valid equivalence partitions for keyword:
     *   - any sequence of starting letters
     *   - keyword containing symbols/numbers
     *   - keyword with leading/trailing spaces
     *
     * Valid equivalence partitions for sentence:
     *   - empty string
     *   - one word
     *   - multiple words
     *   - sentence with extra spaces
     *
     * Possible scenarios returning true:
     *   - keyword matches the first word in sentence
     *   - keyword matches the last word in sentence
     *   - keyword matches the middle word in sentence
     *   - keyword matches multiple words
     *
     * Possible scenarios returning false:
     *   - keyword matches letter in the middle or end of a word
     *   - sentence word start with different letters than the keyword
     *
     * The test method below tries to verify all above with a reasonably low number of test cases.
     */

    @Test
    public void hasWordStartingWithIgnoreCase_validInputs_correctResult() {

        // Empty sentence
        assertFalse(StringUtil.hasWordStartingWithIgnoreCase("", "abc")); // Boundary case
        assertFalse(StringUtil.hasWordStartingWithIgnoreCase("    ", "123"));

        // No word in the sentence begins with the keyword
        assertFalse(StringUtil.hasWordStartingWithIgnoreCase("aad bbb ccc", "d"));

        // Query is longer than any word in the sentence
        assertFalse(StringUtil.hasWordStartingWithIgnoreCase("aaa bbb ccc", "bbbb"));

        // Keyword matches word in the sentence, different upper/lower case letters
        assertTrue(StringUtil.hasWordStartingWithIgnoreCase("aaa bBb ccc", "Bbb")); // Middle word (boundary case)
        assertTrue(StringUtil.hasWordStartingWithIgnoreCase("aaa bBb ccc@1", "CCc@1")); // Last word (boundary case)
        assertTrue(StringUtil.hasWordStartingWithIgnoreCase("  AAA   bBb   ccc  ", "aaa")); // Sentence has extra spaces
        assertTrue(StringUtil.hasWordStartingWithIgnoreCase("Aaa", "aaa")); // Single word in sentence (boundary case)
        assertTrue(StringUtil.hasWordStartingWithIgnoreCase("aaa bbb ccc", "  ccc  ")); // Leading/trailing spaces
        assertTrue(StringUtil.hasWordStartingWithIgnoreCase("aaa bbb ccc", "a")); // Keyword first word (boundary case)
        assertTrue(StringUtil.hasWordStartingWithIgnoreCase("aaa bbb ccc", "Cc")); // Keyword last word (boundary case)

        // Keyword matches multiple words in sentence
        assertTrue(StringUtil.hasWordStartingWithIgnoreCase("AAA bBb ccc  bbb", "bbB"));
    }

    //---------------- Tests for startsWithIgnoreCase --------------------------------------

    /*
     * Invalid equivalence partitions for word: null, empty, multiple words
     * Invalid equivalence partitions for sentence: null
     * The four test cases below test one invalid input at a time.
     */

    @Test
    public void startsWithIgnoreCase_nullWord_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.startsWithIgnoreCase("typical sentence", null));
    }

    @Test
    public void startsWithIgnoreCase_nullSentence_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.startsWithIgnoreCase(null, "abc"));
    }

    /*
     * Valid equivalence partitions for keyword:
     *   - any sequence of starting letters
     *   - keyword containing symbols/numbers
     *   - keyword with leading/trailing spaces
     *
     * Valid equivalence partitions for text:
     *   - empty string
     *   - one word
     *   - word with extra spaces
     *
     * Possible scenarios returning true:
     *   - keyword matches the start of the text
     *
     * Possible scenarios returning false:
     *   - keyword matches letters in the middle or end of the text
     *   - text does not start with the keyword
     *
     * The test method below tries to verify all above with a reasonably low number of test cases.
     */

    @Test
    public void startsWithIgnoreCase_validInputs_correctResult() {

        // Empty sentence
        assertFalse(StringUtil.startsWithIgnoreCase("", "abc")); // Boundary case
        assertFalse(StringUtil.startsWithIgnoreCase("    ", "123"));

        // keyword does not match start of any word
        assertFalse(StringUtil.startsWithIgnoreCase("Hansd", "d")); // keyword match letter at the end of word
        assertFalse(StringUtil.startsWithIgnoreCase("Hans", "bbbbb")); // keyword bigger than text
        assertFalse(StringUtil.startsWithIgnoreCase("Hans", "D")); // keyword smaller than text

        // keyword matches word in the text, different upper/lower case letters
        assertTrue(StringUtil.startsWithIgnoreCase("hans", "HA")); // First keyword (boundary case)
        assertTrue(StringUtil.startsWithIgnoreCase("  HanS   ", "HanS")); // Sentence has extra spaces
        assertTrue(StringUtil.startsWithIgnoreCase("HANS", "  H  ")); // Leading/trailing spaces
        assertTrue(StringUtil.startsWithIgnoreCase("hans", "HANS")); // Capital letters
        assertTrue(StringUtil.startsWithIgnoreCase("h", "H")); // single letter (edge case)
        assertTrue(StringUtil.startsWithIgnoreCase("a", "A")); // single letter (edge case)
    }
}
