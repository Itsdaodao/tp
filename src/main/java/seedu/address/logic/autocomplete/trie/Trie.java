package seedu.address.logic.autocomplete.trie;

import java.util.LinkedList;
import java.util.List;

/**
 * Trie data structure used chiefly for autocomplete feature.
 * This implementation is adapted from
 * (<a href="https://algs4.cs.princeton.edu/52trie/TrieST.java.html">Princeton's TrieST</a>)
 * It contains a subset of features with a 256-way trie for extended ASCII characters.
 */
public class Trie {
    private static final int R = 256; // Use extended ASCII set

    private Node root;

    private static class Node {
        private boolean isWord = false;
        private Node[] next = new Node[R];
    }

    /**
     * Initializes an empty trie.
     */
    public Trie() {

    }

    /**
     * Inserts the key into the trie.
     * @param key the key to insert
     * @throws IllegalArgumentException if key is null
     */
    public void put(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        root = put(root, key, 0);
    }

    private Node put(Node x, String key, int d) {
        if (x == null) {
            x = new Node();
        }
        if (d == key.length()) {
            x.isWord = true;
            return x;
        }
        char c = key.charAt(d);
        x.next[c] = put(x.next[c], key, d + 1);
        return x;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) {
            return null;
        }
        if (d == key.length()) {
            return x;
        }
        char c = key.charAt(d);
        return get(x.next[c], key, d + 1);
    }

    /**
     * Returns all keys in the trie that start with the given prefix.
     * @param prefix the prefix to search for
     * @return a list of keys that start with the given prefix
     * @throws IllegalArgumentException if prefix is null
     */
    public List<String> keysWithPrefix(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("Prefix cannot be null");
        }
        List<String> results = new LinkedList<>();
        Node x = get(root, prefix, 0);
        collect(x, new StringBuilder(prefix), results);
        return results;
    }

    private void collect(Node x, StringBuilder prefix, List<String> results) {
        if (x == null) {
            return;
        }
        if (x.isWord) {
            results.add(prefix.toString());
        }
        for (char c = 0; c < R; c++) {
            prefix.append(c);
            collect(x.next[c], prefix, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }
}
