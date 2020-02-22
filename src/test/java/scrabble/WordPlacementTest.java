package scrabble;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
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

        Board board = new Board();
        board.setTile(7, 7, Tile.H);

        List<WordPlacement> validSet =
                Arrays.asList(
                        // On existing word
                        new WordPlacement(7, 7, WordPlacement.Direction.VERTICAL, "hello"),
                        new WordPlacement(6, 7, WordPlacement.Direction.VERTICAL, "ohello"),

                        // Neighbouring existing word
                        new WordPlacement(7, 6, WordPlacement.Direction.VERTICAL, "hello"),
                        new WordPlacement(6, 6, WordPlacement.Direction.VERTICAL, "hello"),
                        new WordPlacement(8, 7, WordPlacement.Direction.VERTICAL, "hello"),

                        // On existing word
                        new WordPlacement(7, 7, WordPlacement.Direction.HORIZONTAL, "hello"),
                        new WordPlacement(7, 6, WordPlacement.Direction.HORIZONTAL, "ohello"),

                        // Neighbouring existing word
                        new WordPlacement(6, 7, WordPlacement.Direction.HORIZONTAL, "hello"),
                        new WordPlacement(6, 6, WordPlacement.Direction.HORIZONTAL, "hello"),
                        new WordPlacement(7, 8, WordPlacement.Direction.HORIZONTAL, "hello"));

        for (WordPlacement wordPlacement : validSet) {
            assertTrue(wordPlacement.isConnectedToExistingTile(board), wordPlacement.toString());
        }

        List<WordPlacement> invalidSet =
                Arrays.asList(
                        new WordPlacement(9, 7, WordPlacement.Direction.VERTICAL, "hello"),
                        new WordPlacement(7, 9, WordPlacement.Direction.VERTICAL, "hello"),
                        new WordPlacement(9, 7, WordPlacement.Direction.HORIZONTAL, "hello"),
                        new WordPlacement(7, 9, WordPlacement.Direction.HORIZONTAL, "hello"));

        for (WordPlacement wordPlacement : invalidSet) {
            assertFalse(wordPlacement.isConnectedToExistingTile(board), wordPlacement.toString());
        }
    }

    @Test
    void isPlacedAtStar() {}
}
