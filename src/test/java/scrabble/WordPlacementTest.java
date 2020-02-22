package scrabble;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class WordPlacementTest {

    @Test
    void length() {
        WordPlacement wordPlacement =
                new WordPlacement(new BoardPos(0, 0), WordPlacement.Direction.HORIZONTAL, "hello");
        assertEquals(5, wordPlacement.length());
    }

    @Test
    void getPositionAt() {
        {
            WordPlacement wordPlacement =
                    new WordPlacement(new BoardPos(7, 7), WordPlacement.Direction.HORIZONTAL, "hello");

            assertEquals(new BoardPos(7, 7), wordPlacement.getPositionAt(0));
            assertEquals(new BoardPos(7, 9), wordPlacement.getPositionAt(2));
            assertEquals(new BoardPos(7, 11), wordPlacement.getPositionAt(4));

            assertThrows(IndexOutOfBoundsException.class, () -> wordPlacement.getPositionAt(5));
        }

        {
            WordPlacement wordPlacement =
                    new WordPlacement(new BoardPos(7, 7), WordPlacement.Direction.VERTICAL, "hello");

            assertEquals(new BoardPos(7, 7), wordPlacement.getPositionAt(0));
            assertEquals(new BoardPos(9, 7), wordPlacement.getPositionAt(2));
            assertEquals(new BoardPos(11, 7), wordPlacement.getPositionAt(4));

            assertThrows(IndexOutOfBoundsException.class, () -> wordPlacement.getPositionAt(-1));
        }
    }

    @Test
    void getLetterAt() {
        WordPlacement wordPlacement =
                new WordPlacement(new BoardPos(7, 7), WordPlacement.Direction.VERTICAL, "hello");

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
        board.setTile(new BoardPos(7, 7), Tile.H);

        List<WordPlacement> validSet =
                Arrays.asList(
                        // On existing word
                        new WordPlacement(new BoardPos(7, 7), WordPlacement.Direction.VERTICAL, "hello"),
                        new WordPlacement(new BoardPos(6, 7), WordPlacement.Direction.VERTICAL, "ohello"),

                        // Neighbouring existing word
                        new WordPlacement(new BoardPos(7, 6), WordPlacement.Direction.VERTICAL, "hello"),
                        new WordPlacement(new BoardPos(6, 6), WordPlacement.Direction.VERTICAL, "hello"),
                        new WordPlacement(new BoardPos(8, 7), WordPlacement.Direction.VERTICAL, "hello"),

                        // On existing word
                        new WordPlacement(new BoardPos(7, 7), WordPlacement.Direction.HORIZONTAL, "hello"),
                        new WordPlacement(new BoardPos(7, 6), WordPlacement.Direction.HORIZONTAL, "ohello"),

                        // Neighbouring existing word
                        new WordPlacement(new BoardPos(6, 7), WordPlacement.Direction.HORIZONTAL, "hello"),
                        new WordPlacement(new BoardPos(6, 6), WordPlacement.Direction.HORIZONTAL, "hello"),
                        new WordPlacement(new BoardPos(7, 8), WordPlacement.Direction.HORIZONTAL, "hello"));

        for (WordPlacement wordPlacement : validSet) {
            assertTrue(wordPlacement.isConnectedToExistingTile(board), wordPlacement.toString());
        }

        List<WordPlacement> invalidSet =
                Arrays.asList(
                        new WordPlacement(new BoardPos(9, 7), WordPlacement.Direction.VERTICAL, "hello"),
                        new WordPlacement(new BoardPos(7, 9), WordPlacement.Direction.VERTICAL, "hello"),
                        new WordPlacement(new BoardPos(9, 7), WordPlacement.Direction.HORIZONTAL, "hello"),
                        new WordPlacement(new BoardPos(7, 9), WordPlacement.Direction.HORIZONTAL, "hello"));

        for (WordPlacement wordPlacement : invalidSet) {
            assertFalse(wordPlacement.isConnectedToExistingTile(board), wordPlacement.toString());
        }
    }

    @Test
    void isPlacedAtStar() {

        Board board = new Board();

        List<WordPlacement> validSet =
                Arrays.asList(
                        // On existing word
                        new WordPlacement(new BoardPos(7, 7), WordPlacement.Direction.VERTICAL, "hello"),
                        new WordPlacement(new BoardPos(6, 7), WordPlacement.Direction.VERTICAL, "ohello"),

                        // On existing word
                        new WordPlacement(new BoardPos(7, 7), WordPlacement.Direction.HORIZONTAL, "hello"),
                        new WordPlacement(new BoardPos(7, 6), WordPlacement.Direction.HORIZONTAL, "ohello"));

        for (WordPlacement wordPlacement : validSet) {
            assertTrue(wordPlacement.isPlacedAtStar(board), wordPlacement.toString());
        }

        List<WordPlacement> invalidSet =
                Arrays.asList(
                        new WordPlacement(new BoardPos(9, 7), WordPlacement.Direction.VERTICAL, "hello"),
                        new WordPlacement(new BoardPos(7, 9), WordPlacement.Direction.VERTICAL, "hello"),
                        new WordPlacement(new BoardPos(9, 7), WordPlacement.Direction.HORIZONTAL, "hello"),
                        new WordPlacement(new BoardPos(7, 9), WordPlacement.Direction.HORIZONTAL, "hello"),

                        // Neighbouring existing word
                        new WordPlacement(new BoardPos(7, 6), WordPlacement.Direction.VERTICAL, "hello"),
                        new WordPlacement(new BoardPos(6, 6), WordPlacement.Direction.VERTICAL, "hello"),
                        new WordPlacement(new BoardPos(8, 7), WordPlacement.Direction.VERTICAL, "hello"),

                        // Neighbouring existing word
                        new WordPlacement(new BoardPos(6, 7), WordPlacement.Direction.HORIZONTAL, "hello"),
                        new WordPlacement(new BoardPos(6, 6), WordPlacement.Direction.HORIZONTAL, "hello"),
                        new WordPlacement(new BoardPos(7, 8), WordPlacement.Direction.HORIZONTAL, "hello"));

        for (WordPlacement wordPlacement : invalidSet) {
            assertFalse(wordPlacement.isPlacedAtStar(board), wordPlacement.toString());
        }
    }
}
