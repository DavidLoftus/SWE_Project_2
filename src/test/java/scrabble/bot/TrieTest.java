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
        trie.add("helloworld");
        trie.add("hello+world");

        assertThrows(IllegalArgumentException.class, () -> trie.add("hello world"));
        assertThrows(IllegalArgumentException.class, () -> trie.add("helloworld!"));
    }

    @Test
    void addArc() {
        Trie subTrie = trie.addArc('h');
        assertEquals(subTrie, trie.get('h'));

        Trie nestedSubTrie = subTrie.addArc('+');
        assertEquals(nestedSubTrie, subTrie.get('+'));
        assertEquals(nestedSubTrie, trie.get("h+"));

        assertThrows(IllegalArgumentException.class, () -> trie.addArc('?'));
    }

    @Test
    void addFinalArc() {
        trie.addFinalArc('a', 'b');
        assertTrue(trie.contains("ab"));
        assertFalse(trie.contains("a"));
        assertFalse(trie.contains("b"));
        assertFalse(trie.contains("ba"));

        assertThrows(IllegalArgumentException.class, () -> trie.addFinalArc('?', 'a'));
        assertThrows(IllegalArgumentException.class, () -> trie.addFinalArc('b', '?'));
    }

    @Test
    void forceArc() {
        Trie subTrie = trie.addArc('h');
        Trie nestedSubTrie = subTrie.addArc('+');
        nestedSubTrie.addFinalArc('b', 'c');

        trie.forceArc('a', nestedSubTrie);
        assertTrue(trie.contains("h+bc"));
        assertTrue(trie.contains("abc"));

        assertThrows(IllegalArgumentException.class, () -> trie.addArc('?'));
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

        {
            Iterator<String> iter = trie.iterator();

            assertTrue(iter.hasNext());
            assertEquals("hello+world", iter.next());

            assertTrue(iter.hasNext());
            assertEquals("helloworld", iter.next());

            assertFalse(iter.hasNext());
        }

        {
            Trie subTrie = trie.get("hello");
            Iterator<String> iter = trie.iterator();

            assertTrue(iter.hasNext());
            assertEquals("+world", iter.next());

            assertTrue(iter.hasNext());
            assertEquals("world", iter.next());

            assertFalse(iter.hasNext());
        }

        {
            Trie subTrie = trie.get("hello+");
            Iterator<String> iter = trie.iterator();

            assertTrue(iter.hasNext());
            assertEquals("world", iter.next());

            assertFalse(iter.hasNext());
        }
    }
}
