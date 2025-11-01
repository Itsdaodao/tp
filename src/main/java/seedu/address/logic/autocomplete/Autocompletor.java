package seedu.address.logic.autocomplete;

import java.util.List;

import seedu.address.logic.autocomplete.trie.Trie;
import seedu.address.logic.commands.CommandRegistry;

/**
 * Autocompletor class that provides autocomplete hints based on command words.
 */
public class Autocompletor {
    private final Trie trie;

    /**
     * Constructs an Autocompletor and initializes possible completions with command words.
     */
    public Autocompletor() {
        trie = new Trie();
        for (String c : CommandRegistry.getCommandWords()) {
            trie.put(c);
        }
    }

    /**
     * Returns the first autocomplete hint for the given input.
     * If there are no matches, returns an empty string.
     */
    public String getHint(String input) {
        if (input.isBlank()) {
            return "";
        }
        String trimmed = input.replaceAll("^(\\s+)", "");
        String leadingWhitespace = input.substring(0, input.length() - trimmed.length());
        List<String> matches = this.trie.keysWithPrefix(trimmed);
        if (matches.isEmpty()) {
            return "";
        }
        // Preserve the original whitespace in the input
        return leadingWhitespace + matches.get(0);
    }

}
