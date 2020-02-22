package scrabble;

import org.graalvm.compiler.word.Word;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class WordPlacementTest {

    @Test
    void length() {
        WordPlacement wordPlacement =
                new WordPlacement(0, 0, WordPlacement.Direction.HORIZONTAL, "hello");
        assertEquals(5, wordPlacement.length());
    }

    @Test
    void getRowForLetter() {
        {
            WordPlacement wordPlacement =
                    new WordPlacement(7, 7, WordPlacement.Direction.HORIZONTAL, "hello");

            assertEquals(7, wordPlacement.getRowForLetter(0));
            assertEquals(7, wordPlacement.getRowForLetter(2));

            assertThrows(IndexOutOfBoundsException.class, () -> wordPlacement.getRowForLetter(5));
        }

        {
            WordPlacement wordPlacement =
                    new WordPlacement(7, 7, WordPlacement.Direction.VERTICAL, "hello");

            assertEquals(7, wordPlacement.getRowForLetter(0));
            assertEquals(9, wordPlacement.getRowForLetter(2));
            assertEquals(11, wordPlacement.getRowForLetter(4));

            assertThrows(IndexOutOfBoundsException.class, () -> wordPlacement.getRowForLetter(-1));
        }
    }

    @Test
    void getColumnForLetter() {
        {
            WordPlacement wordPlacement =
                    new WordPlacement(7, 7, WordPlacement.Direction.VERTICAL, "hello");

            assertEquals(7, wordPlacement.getColumnForLetter(0));
            assertEquals(7, wordPlacement.getColumnForLetter(2));

            assertThrows(
                    IndexOutOfBoundsException.class, () -> wordPlacement.getColumnForLetter(5));
        }

        {
            WordPlacement wordPlacement =
                    new WordPlacement(7, 7, WordPlacement.Direction.HORIZONTAL, "hello");

            assertEquals(7, wordPlacement.getColumnForLetter(0));
            assertEquals(9, wordPlacement.getColumnForLetter(2));
            assertEquals(11, wordPlacement.getColumnForLetter(4));

            assertThrows(
                    IndexOutOfBoundsException.class, () -> wordPlacement.getColumnForLetter(-1));
        }
    }

    @Test
    void getLetterAt() {
        WordPlacement wordPlacement =
                new WordPlacement(7, 7, WordPlacement.Direction.VERTICAL, "hello");

        assertEquals('H', wordPlacement.getLetterAt(0));
        assertEquals('E', wordPlacement.getLetterAt(1));
        assertEquals('L', wordPlacement.getLetterAt(2));
        assertEquals('L', wordPlacement.getLetterAt(3));
        assertEquals('O', wordPlacement.getLetterAt(4));

        assertThrows(IndexOutOfBoundsException.class, () -> wordPlacement.getLetterAt(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> wordPlacement.getLetterAt(5));
    }

    @Test
    void isConnectedToExistingTile() {
    }

    @Test
    void isPlacedAtStar() {
    }
}