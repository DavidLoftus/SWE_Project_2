package scrabble.wordlist;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SowpodsTest {

    private static Sowpods sowpods;

    @BeforeAll
    static void initSowpos() throws IOException {
        sowpods = new Sowpods();
    }

    @Test
    void testValidWords() {
        assertTrue(sowpods.isValidWord("CAT"));
    }

    @Test
    void testInvalidWords() {
        assertFalse(sowpods.isValidWord("AFJASIOFJOISG"));
    }
}