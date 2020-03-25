package scrabble;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class WordPlacementTest {

    @Test
    void length() {
        WordPlacement wordPlacement =
                new WordPlacement(new BoardPos(0, 0), WordPlacement.Direction.HORIZONTAL, "hello");
        assertEquals(5, wordPlacement.length());
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
}
