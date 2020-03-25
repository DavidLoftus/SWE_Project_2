package scrabble;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

public class WordRangeTest {

    @Test
    void getPositionAt() {
        {
            WordRange wordRange =
                    new WordRange(new BoardPos(7, 7), WordRange.Direction.HORIZONTAL, 5);

            assertEquals(new BoardPos(7, 7), wordRange.getPositionAt(0));
            assertEquals(new BoardPos(7, 9), wordRange.getPositionAt(2));
            assertEquals(new BoardPos(7, 11), wordRange.getPositionAt(4));

            assertThrows(IndexOutOfBoundsException.class, () -> wordRange.getPositionAt(5));
        }

        {
            WordPlacement wordPlacement =
                    new WordPlacement(
                            new BoardPos(7, 7), WordPlacement.Direction.VERTICAL, "hello");

            assertEquals(new BoardPos(7, 7), wordPlacement.getPositionAt(0));
            assertEquals(new BoardPos(9, 7), wordPlacement.getPositionAt(2));
            assertEquals(new BoardPos(11, 7), wordPlacement.getPositionAt(4));

            assertThrows(IndexOutOfBoundsException.class, () -> wordPlacement.getPositionAt(-1));
        }
    }

    @Test
    void isConnectedToExistingTile() {

        Board board = new Board();
        board.setTile(new BoardPos(7, 7), Tile.H);

        List<WordRange> validSet =
                Arrays.asList(
                        // On existing word
                        new WordRange(new BoardPos(7, 7), WordRange.Direction.VERTICAL, 5),
                        new WordRange(new BoardPos(6, 7), WordRange.Direction.VERTICAL, 6),

                        // Neighbouring existing word
                        new WordRange(new BoardPos(7, 6), WordRange.Direction.VERTICAL, 5),
                        new WordRange(new BoardPos(6, 6), WordRange.Direction.VERTICAL, 5),
                        new WordRange(new BoardPos(8, 7), WordRange.Direction.VERTICAL, 5),

                        // On existing word
                        new WordRange(new BoardPos(7, 7), WordRange.Direction.HORIZONTAL, 5),
                        new WordRange(new BoardPos(7, 6), WordRange.Direction.HORIZONTAL, 6),

                        // Neighbouring existing word
                        new WordRange(new BoardPos(6, 7), WordRange.Direction.HORIZONTAL, 5),
                        new WordRange(new BoardPos(6, 6), WordRange.Direction.HORIZONTAL, 5),
                        new WordRange(new BoardPos(7, 8), WordRange.Direction.HORIZONTAL, 5));

        for (WordRange wordRange : validSet) {
            assertTrue(wordRange.isConnectedToExistingTile(board), wordRange.toString());
        }

        List<WordRange> invalidSet =
                Arrays.asList(
                        new WordRange(new BoardPos(9, 7), WordRange.Direction.VERTICAL, 5),
                        new WordRange(new BoardPos(7, 9), WordRange.Direction.VERTICAL, 5),
                        new WordRange(new BoardPos(9, 7), WordRange.Direction.HORIZONTAL, 5),
                        new WordRange(new BoardPos(7, 9), WordRange.Direction.HORIZONTAL, 5));

        for (WordRange wordRange : invalidSet) {
            assertFalse(wordRange.isConnectedToExistingTile(board), wordRange.toString());
        }
    }

    @Test
    void isPlacedAtStar() {

        Board board = new Board();

        List<WordRange> validSet =
                Arrays.asList(
                        // On existing word
                        new WordRange(new BoardPos(7, 7), WordRange.Direction.VERTICAL, 5),
                        new WordRange(new BoardPos(6, 7), WordRange.Direction.VERTICAL, 6),

                        // On existing word
                        new WordRange(new BoardPos(7, 7), WordRange.Direction.HORIZONTAL, 5),
                        new WordRange(new BoardPos(7, 6), WordRange.Direction.HORIZONTAL, 6));

        for (WordRange wordRange : validSet) {
            assertTrue(wordRange.isPlacedAtStar(board), wordRange.toString());
        }

        List<WordRange> invalidSet =
                Arrays.asList(
                        new WordRange(new BoardPos(9, 7), WordRange.Direction.VERTICAL, 5),
                        new WordRange(new BoardPos(7, 9), WordRange.Direction.VERTICAL, 5),
                        new WordRange(new BoardPos(9, 7), WordRange.Direction.HORIZONTAL, 5),
                        new WordRange(new BoardPos(7, 9), WordRange.Direction.HORIZONTAL, 5),

                        // Neighbouring existing word
                        new WordRange(new BoardPos(7, 6), WordRange.Direction.VERTICAL, 5),
                        new WordRange(new BoardPos(6, 6), WordRange.Direction.VERTICAL, 5),
                        new WordRange(new BoardPos(8, 7), WordRange.Direction.VERTICAL, 5),

                        // Neighbouring existing word
                        new WordRange(new BoardPos(6, 7), WordRange.Direction.HORIZONTAL, 5),
                        new WordRange(new BoardPos(6, 6), WordRange.Direction.HORIZONTAL, 5),
                        new WordRange(new BoardPos(7, 8), WordRange.Direction.HORIZONTAL, 5));

        for (WordRange wordRange : invalidSet) {
            assertFalse(wordRange.isPlacedAtStar(board), wordRange.toString());
        }
    }
}
