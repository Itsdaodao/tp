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
    public static final String ERROR_EMPTY_KEYWORD = "Keyword parameter cannot be empty";
    public static final String ERROR_MULTIPLE_WORDS = "Keyword parameter should be a single word";
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
        checkArgument(!preppedWord.isEmpty(), ERROR_EMPTY_KEYWORD);
        checkArgument(preppedWord.split("\\s+").length == 1, ERROR_MULTIPLE_WORDS);

        String preppedSentence = sentence;
        String[] wordsInPreppedSentence = preppedSentence.split("\\s+");

        return Arrays.stream(wordsInPreppedSentence)
                .anyMatch(preppedWord::equalsIgnoreCase);
    }

    /**
     * Returns true if any word in the given {@code sentence} starts with the specific {@code keyword},
     * ignoring case differences.
     *   <br>examples:<pre>
     *       containsWordStartingWithIgnoreCase("Hans Tho, "Hans") == true
     *       containsWordStartingWithIgnoreCase("Hans Tho", "THO") == true
     *       containsWordStartingWithIgnoreCase("Hans Tho", "hans") == true
     *       containsWordStartingWithIgnoreCase("Hans Tho", "ha") == true
     *       containsWordStartingWithIgnoreCase("Hans Tho", "s") == false
     *       </pre>
     *
     * @param sentence sentence to search in
     * @param keyword keyword to match at the start of any word
     * @return {@code true} if any word in the sentence starts with the keyword; {@code false} otherwise
     * @throws NullPointerException If {@code sentence} or {@code keyword} is {@code null}.
     * @throws IllegalArgumentException If {@code keyword} is empty or contains multiple words.
     */
    public static boolean hasWordStartingWithIgnoreCase(String sentence, String keyword) {
        requireNonNull(sentence);
        requireNonNull(keyword);

        String preppedWord = keyword.trim();
        checkArgument(!preppedWord.isEmpty(), ERROR_EMPTY_KEYWORD);
        checkArgument(preppedWord.split("\\s+").length == 1, ERROR_MULTIPLE_WORDS);

        String preppedSentence = sentence;
        String[] wordsInPreppedSentence = preppedSentence.split("\\s+");

        boolean hasWordStartingWithKeyword = Arrays.stream(wordsInPreppedSentence)
                .anyMatch(word -> startsWithIgnoreCase(word, preppedWord));

        return hasWordStartingWithKeyword;
    }

    /**
     * Checks if the given text starts with the specified keyword, ignoring case differences.
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
     * @param keyword the text to check against the start of {@code text}; must not be null or empty
     * @return true if the text starts with the prefix (case-insensitive), false otherwise
     */
    public static boolean startsWithIgnoreCase(String text, String keyword) {
        requireNonNull(text);
        requireNonNull(keyword);

        String trimmedText = text.trim();
        String trimmedPrefix = keyword.trim();

        boolean isTextStartingWithKeyword = trimmedText.toLowerCase().startsWith(trimmedPrefix.toLowerCase());

        return isTextStartingWithKeyword;
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
