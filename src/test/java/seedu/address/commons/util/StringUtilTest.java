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
        assertThrows(IllegalArgumentException.class, "Word parameter cannot be empty", ()
            -> StringUtil.containsWordIgnoreCase("typical sentence", "  "));
    }

    @Test
    public void containsWordIgnoreCase_multipleWords_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, "Word parameter should be a single word", ()
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

    //---------------- Tests for containsPrefixIgnoreCase --------------------------------------

    /*
     * Invalid equivalence partitions for word: null, empty, multiple words
     * Invalid equivalence partitions for sentence: null
     * The four test cases below test one invalid input at a time.
     */

    @Test
    public void containsPrefixIgnoreCase_nullWord_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.containsPrefixIgnoreCase("typical sentence", null));
    }

    @Test
    public void containsPrefixIgnoreCase_emptyWord_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, "Keyword parameter cannot be empty", ()
                -> StringUtil.containsPrefixIgnoreCase("typical sentence", "  "));
    }

    @Test
    public void containsPrefixIgnoreCase_multipleWords_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, "Keyword parameter should be a single word", ()
                -> StringUtil.containsPrefixIgnoreCase("typical sentence", "aaa BBB"));
    }

    @Test
    public void containsPrefixIgnoreCase_nullSentence_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.containsPrefixIgnoreCase(null, "abc"));
    }

    /*
     * Valid equivalence partitions for keyword:
     *   - any sequence of starting letters (prefix)
     *   - prefix containing symbols/numbers
     *   - prefix with leading/trailing spaces
     *
     * Valid equivalence partitions for sentence:
     *   - empty string
     *   - one word
     *   - multiple words
     *   - sentence with extra spaces
     *
     * Possible scenarios returning true:
     *   - prefix matches the first word in sentence
     *   - prefix matches the last word in sentence
     *   - prefix matches the middle word in sentence
     *   - prefix matches multiple words
     *
     * Possible scenarios returning false:
     *   - prefix matches letter in the middle or end of a word
     *   - sentence word start with different letters than the prefix
     *
     * The test method below tries to verify all above with a reasonably low number of test cases.
     */

    @Test
    public void containsPrefixIgnoreCase_validInputs_correctResult() {

        // Empty sentence
        assertFalse(StringUtil.containsPrefixIgnoreCase("", "abc")); // Boundary case
        assertFalse(StringUtil.containsPrefixIgnoreCase("    ", "123"));

        // Prefix does not match start of any word
        assertFalse(StringUtil.containsPrefixIgnoreCase("aad bbb ccc", "d")); // prefix match letter at the end of word
        assertFalse(StringUtil.containsPrefixIgnoreCase("aaa bbb ccc", "bbbb")); // Query word bigger than sentence word

        // Prefix matches word in the sentence, different upper/lower case letters
        assertTrue(StringUtil.containsPrefixIgnoreCase("aaa bBb ccc", "Bbb")); // First word (boundary case)
        assertTrue(StringUtil.containsPrefixIgnoreCase("aaa bBb ccc@1", "CCc@1")); // Last word (boundary case)
        assertTrue(StringUtil.containsPrefixIgnoreCase("  AAA   bBb   ccc  ", "aaa")); // Sentence has extra spaces
        assertTrue(StringUtil.containsPrefixIgnoreCase("Aaa", "aaa")); // Only one word in sentence (boundary case)
        assertTrue(StringUtil.containsPrefixIgnoreCase("aaa bbb ccc", "  ccc  ")); // Leading/trailing spaces
        assertTrue(StringUtil.containsPrefixIgnoreCase("aaa bbb ccc", "a")); // Prefix first word (boundary case)
        assertTrue(StringUtil.containsPrefixIgnoreCase("aaa bbb ccc", "Cc")); // Prefix last word (boundary case)

        // Prefix matches multiple words in sentence
        assertTrue(StringUtil.containsPrefixIgnoreCase("AAA bBb ccc  bbb", "bbB"));
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
     * Valid equivalence partitions for prefix:
     *   - any sequence of starting letters (prefix)
     *   - prefix containing symbols/numbers
     *   - prefix with leading/trailing spaces
     *
     * Valid equivalence partitions for text:
     *   - empty string
     *   - one word
     *   - word with extra spaces
     *
     * Possible scenarios returning true:
     *   - prefix matches the start of the text
     *
     * Possible scenarios returning false:
     *   - prefix matches letters in the middle or end of the text
     *   - text does not start with the prefix
     *
     * The test method below tries to verify all above with a reasonably low number of test cases.
     */

    @Test
    public void startsWithIgnoreCase_validInputs_correctResult() {

        // Empty sentence
        assertFalse(StringUtil.startsWithIgnoreCase("", "abc")); // Boundary case
        assertFalse(StringUtil.startsWithIgnoreCase("    ", "123"));

        // Prefix does not match start of any word
        assertFalse(StringUtil.startsWithIgnoreCase("Hans", "d")); // prefix match letter at the end of word
        assertFalse(StringUtil.startsWithIgnoreCase("Hans", "bbbbb")); // prefix bigger than text
        assertFalse(StringUtil.startsWithIgnoreCase("Hans", "D")); // Prefix smaller than text

        // Prefix matches word in the text, different upper/lower case letters
        assertTrue(StringUtil.startsWithIgnoreCase("hans", "HA")); // First Prefix (boundary case)
        assertTrue(StringUtil.startsWithIgnoreCase("  HanS   ", "HanS")); // Sentence has extra spaces
        assertTrue(StringUtil.startsWithIgnoreCase("HANS", "  H  ")); // Leading/trailing spaces
        assertTrue(StringUtil.startsWithIgnoreCase("hans", "HANS")); // Capital letters
        assertTrue(StringUtil.startsWithIgnoreCase("h", "H")); // single letter (edge case)
        assertTrue(StringUtil.startsWithIgnoreCase("a", "A")); // single letter (edge case)
    }
}
