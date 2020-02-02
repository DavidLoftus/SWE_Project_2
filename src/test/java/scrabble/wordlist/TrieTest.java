package scrabble.wordlist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TrieTest {

    private Trie trie;

    @BeforeEach
    void init() {
        trie = new Trie();
    }

    @Test
    void add() {
        trie.add("helloworld");
        trie.add("hello+world");

        assertThrows(IllegalArgumentException.class, () -> trie.add("hello world"));
        assertThrows(IllegalArgumentException.class, () -> trie.add("helloworld!"));
    }

    @Test
    void get() {
        trie.add("helloworld");
        trie.add("hello+world");

        Trie subTrie = trie.get("hello");
        assertThrows(NoSuchElementException.class, () -> trie.get("baby"));
        assertThrows(IllegalArgumentException.class, () -> trie.get("hello "));

        assertNotNull(subTrie.get("worl"));
        assertNotNull(subTrie.get("+worl"));

        assertThrows(NoSuchElementException.class, () -> subTrie.get("baby"));
    }

    @Test
    void contains() {
        trie.add("helloworld");
        trie.add("hello+world");

        Trie subTrie = trie.get("hello");

        assertTrue(subTrie.contains("world"));
        assertTrue(subTrie.contains("+world"));

        assertFalse(subTrie.contains("worl"));
        assertFalse(subTrie.contains("+worl"));

        assertFalse(trie.contains("goodbyeworld"));
    }

    @Test
    void iterator() {
        trie.add("helloworld");
        trie.add("hello+world");

        Iterator<String> iter = trie.iterator();

        assertTrue(iter.hasNext());
        assertEquals("helloworld", iter.next());

        assertTrue(iter.hasNext());
        assertEquals("hello+world", iter.next());

        assertFalse(iter.hasNext());
    }

    @Test
    void collectAll() {
        trie.add("helloworld");
        trie.add("hello+world");

        Set<String> list = trie.collectAll();

        assertEquals(2, list.size());

        assertTrue(list.contains("hello+world"));
        assertTrue(list.contains("helloworld"));

    }
}