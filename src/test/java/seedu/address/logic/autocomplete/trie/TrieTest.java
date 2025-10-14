package seedu.address.logic.autocomplete.trie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class TrieTest {
    @Test
    public void trie_hasMatchingPrefixes_returnsPrefixes() {
        Trie trie = new Trie();
        trie.put("conspicuous");
        trie.put("contraceptives");
        trie.put("contradict");
        trie.put("consciousness");
        trie.put("ycaripsnoc");

        List<String> din = trie.keysWithPrefix("con");

        assertEquals(4, din.size());
        assertTrue(din.contains("conspicuous"));
        assertTrue(din.contains("contraceptives"));
        assertTrue(din.contains("contradict"));
        assertTrue(din.contains("consciousness"));
    }

    @Test
    public void trie_noMatchingPrefixes_returnsEmpty() {
        Trie trie = new Trie();
        trie.put("conspicuous");
        trie.put("contraceptives");
        trie.put("contradict");
        trie.put("consciousness");
        trie.put("ycaripsnoc");

        List<String> din = trie.keysWithPrefix("xyz");

        assertEquals(0, din.size());
    }

    @Test
    public void trie_insertingNull_throwsException() {
        Trie trie = new Trie();

        assertThrows(IllegalArgumentException.class, () -> trie.put(null));
    }

    @Test
    public void trie_prefixSearchNull_throwsException() {
        Trie trie = new Trie();

        assertThrows(IllegalArgumentException.class, () -> trie.keysWithPrefix(null));
    }
}
