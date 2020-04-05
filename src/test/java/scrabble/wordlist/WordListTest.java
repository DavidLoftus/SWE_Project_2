package scrabble.wordlist;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class WordListTest {

    public static WordList wordList;

    @BeforeAll
    static void init() {
        wordList = new WordList();
    }

    @Test
    void testValidWords() {
        String[] words =
                new String[] {
                    "cat", "crocodile", "CAR", "cRaZy", "abacus", "word", "list",
                };
        for (String word : words) {
            assertTrue(wordList.isValidWord(word), word);
        }
    }

    @Test
    void testInvalidWords() {
        String[] words =
                new String[] {
                    "cfhjsdi", "", "k", "word list",
                };
        for (String word : words) {
            assertFalse(wordList.isValidWord(word), word);
        }
    }
}
