package scrabble;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BoardTest {

    @Test
    void setTile() {
        Board board = new Board();
        assertThrows(IllegalArgumentException.class, () -> board.setTile(7, 7, Tile.BLANK));
    }

    @Test
    void getLetterAt() {
        Board board = new Board();
        board.setTile(7, 7, Tile.E);
        assertEquals('E', board.getLetterAt(7, 7));
        board.setTile(7, 8, Tile.A);
        assertEquals('A', board.getLetterAt(7, 8));
        board.setTile(7, 9, Tile.D);
        assertEquals('D', board.getLetterAt(7, 9));
    }

    @Test
    void reset() {
        Board board = new Board();
        board.setTile(7, 7, Tile.E);
        board.reset();
        assertThrows(IllegalArgumentException.class, () -> board.getLetterAt(7, 7));
    }
}
