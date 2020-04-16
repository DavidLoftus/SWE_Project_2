package scrabble.bot;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrieTest {

    private Trie trie;

    @BeforeEach
    void init() {
        this.trie = new Trie();
    }

    @Test
    void add() {
        trie.add("HELLOWORLD");
        trie.add("HELLO+WORLD");

        assertThrows(IllegalArgumentException.class, () -> trie.add("HELLO WORLD"));
        assertThrows(IllegalArgumentException.class, () -> trie.add("HELLOWORLD!"));
    }

    @Test
    void addArc() {
        Trie subTrie = trie.addArc('H');
        assertEquals(subTrie, trie.get('H'));

        Trie nestedSubTrie = subTrie.addArc('+');
        assertEquals(nestedSubTrie, subTrie.get('+'));
        assertEquals(nestedSubTrie, trie.get("H+"));

        assertThrows(IllegalArgumentException.class, () -> trie.addArc('?'));
    }

    @Test
    void addFinalArc() {
        trie.addFinalArc('A', 'B');
        assertTrue(trie.contains("AB"));
        assertFalse(trie.contains("A"));
        assertFalse(trie.contains("B"));
        assertFalse(trie.contains("BA"));

        assertThrows(IllegalArgumentException.class, () -> trie.addFinalArc('?', 'A'));
        assertThrows(IllegalArgumentException.class, () -> trie.addFinalArc('B', '?'));
    }

    @Test
    void forceArc() {
        Trie subTrie = trie.addArc('H');
        Trie nestedSubTrie = subTrie.addArc('+');
        nestedSubTrie.addFinalArc('B', 'C');

        trie.forceArc('A', nestedSubTrie);
        assertTrue(trie.contains("H+BC"));
        assertTrue(trie.contains("ABC"));

        assertThrows(IllegalArgumentException.class, () -> trie.addArc('?'));
    }

    @Test
    void get() {
        trie.add("HELLOWORLD");
        trie.add("HELLO+WORLD");

        Trie subTrie = trie.get("HELLO");
        assertThrows(NoSuchElementException.class, () -> trie.get("BABY"));
        assertThrows(IllegalArgumentException.class, () -> trie.get("HELLO "));

        assertNotNull(subTrie.get("WORL"));
        assertNotNull(subTrie.get("+WORL"));

        assertThrows(NoSuchElementException.class, () -> subTrie.get("BABY"));
    }

    @Test
    void contains() {
        trie.add("HELLOWORLD");
        trie.add("HELLO+WORLD");

        Trie subTrie = trie.get("HELLO");

        assertTrue(subTrie.contains("WORLD"));
        assertTrue(subTrie.contains("+WORLD"));

        assertFalse(subTrie.contains("WORL"));
        assertFalse(subTrie.contains("+WORL"));

        assertFalse(trie.contains("GOODBYEWORLD"));
    }

    @Test
    void iterator() {
        trie.add("HELLOWORLD");
        trie.add("HELLO+WORLD");

        {
            Iterator<String> iter = trie.iterator();

            assertTrue(iter.hasNext());
            assertEquals("HELLOWORLD", iter.next());

            assertTrue(iter.hasNext());
            assertEquals("HELLO+WORLD", iter.next());

            assertFalse(iter.hasNext());
        }

        {
            Trie subTrie = trie.get("HELLO");
            Iterator<String> iter = subTrie.iterator();

            assertTrue(iter.hasNext());
            assertEquals("WORLD", iter.next());

            assertTrue(iter.hasNext());
            assertEquals("+WORLD", iter.next());

            assertFalse(iter.hasNext());
        }

        {
            Trie subTrie = trie.get("HELLO+");
            Iterator<String> iter = subTrie.iterator();

            assertTrue(iter.hasNext());
            assertEquals("WORLD", iter.next());

            assertFalse(iter.hasNext());
        }
    }
}
