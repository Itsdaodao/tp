package seedu.address.commons.util;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

/**
 * Helper functions for handling strings.
 */
public class StringUtil {

    /**
     * Returns true if the {@code sentence} contains the {@code word}.
     *   Ignores case, but a full word match is required.
     *   <br>examples:<pre>
     *       containsWordIgnoreCase("ABc def", "abc") == true
     *       containsWordIgnoreCase("ABc def", "DEF") == true
     *       containsWordIgnoreCase("ABc def", "AB") == false //not a full word match
     *       </pre>
     * @param sentence cannot be null
     * @param word cannot be null, cannot be empty, must be a single word
     */
    public static boolean containsWordIgnoreCase(String sentence, String word) {
        requireNonNull(sentence);
        requireNonNull(word);

        String preppedWord = word.trim();
        checkArgument(!preppedWord.isEmpty(), "Word parameter cannot be empty");
        checkArgument(preppedWord.split("\\s+").length == 1, "Word parameter should be a single word");

        String preppedSentence = sentence;
        String[] wordsInPreppedSentence = preppedSentence.split("\\s+");

        return Arrays.stream(wordsInPreppedSentence)
                .anyMatch(preppedWord::equalsIgnoreCase);
    }

    /**
     * Returns true if the {@code sentence} starts with the {@code word}.
     *   Ignores case, but a prefix match is required.
     *   <br>examples:<pre>
     *       containsPrefixIgnoreCase("Hans Tho, "Hans") == true
     *       containsPrefixIgnoreCase("Hans Tho", "THO") == true
     *       containsPrefixIgnoreCase("Hans Tho", "hans") == true
     *       containsPrefixIgnoreCase("Hans Tho", "ha") == true
     *       containsPrefixIgnoreCase("Hans Tho", "s") == false
     *       </pre>
     * @param sentence cannot be null
     * @param keyword cannot be null, cannot be empty, must be a single word
     */
    public static boolean containsPrefixIgnoreCase(String sentence, String keyword) {
        requireNonNull(sentence);
        requireNonNull(keyword);

        String preppedWord = keyword.trim();
        checkArgument(!preppedWord.isEmpty(), "Keyword parameter cannot be empty");
        checkArgument(preppedWord.split("\\s+").length == 1,
                "Keyword parameter should be a single word");

        String preppedSentence = sentence;
        String[] wordsInPreppedSentence = preppedSentence.split("\\s+");

        return Arrays.stream(wordsInPreppedSentence)
                .anyMatch(word -> startsWithIgnoreCase(word, preppedWord));
    }

    /**
     * Checks if the given text starts with the specified prefix, ignoring case differences.
     *
     * <p>Examples:
     * <pre>
     * startsWithIgnoreCase("Hans", "h")    // true
     * startsWithIgnoreCase("Hans", "Ha")   // true
     * startsWithIgnoreCase("Hans", "han")  // true
     * startsWithIgnoreCase("Hans", "HANS") // true
     * startsWithIgnoreCase("Hans", "a")    // false
     * </pre>
     *
     * @param text   the full text to check; must not be null
     * @param prefix the prefix to test for; must not be null or empty
     * @return true if the text starts with the prefix (case-insensitive), false otherwise
     */
    public static boolean startsWithIgnoreCase(String text, String prefix) {
        requireNonNull(text);
        requireNonNull(prefix);

        String trimmedText = text.trim();
        String trimmedPrefix = prefix.trim();

        return trimmedText.toLowerCase().startsWith(trimmedPrefix.toLowerCase());
    }

    /**
     * Returns a detailed message of the t, including the stack trace.
     */
    public static String getDetails(Throwable t) {
        requireNonNull(t);
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return t.getMessage() + "\n" + sw.toString();
    }

    /**
     * Returns true if {@code s} represents a non-zero unsigned integer
     * e.g. 1, 2, 3, ..., {@code Integer.MAX_VALUE} <br>
     * Will return false for any other non-null string input
     * e.g. empty string, "-1", "0", "+1", and " 2 " (untrimmed), "3 0" (contains whitespace), "1 a" (contains letters)
     * @throws NullPointerException if {@code s} is null.
     */
    public static boolean isNonZeroUnsignedInteger(String s) {
        requireNonNull(s);

        try {
            int value = Integer.parseInt(s);
            return value > 0 && !s.startsWith("+"); // "+1" is successfully parsed by Integer#parseInt(String)
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
