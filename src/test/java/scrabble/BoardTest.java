package scrabble;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import scrabble.exceptions.BadWordPlacementException;

public class BoardTest {

    @Test
    void setTile() {
        Board board = new Board();
        assertThrows(
                IllegalArgumentException.class,
                () -> board.setTile(new BoardPos(7, 7), Tile.BLANK));
    }

    @Test
    void getLetterAt() {
        Board board = new Board();
        board.setTile(new BoardPos(7, 7), Tile.E);
        assertEquals('E', board.getLetterAt(new BoardPos(7, 7)));
        board.setTile(new BoardPos(7, 8), Tile.A);
        assertEquals('A', board.getLetterAt(new BoardPos(7, 8)));
        board.setTile(new BoardPos(7, 9), Tile.D);
        assertEquals('D', board.getLetterAt(new BoardPos(7, 9)));
    }

    @Test
    void reset() {
        Board board = new Board();
        board.setTile(new BoardPos(7, 7), Tile.E);
        board.reset();
        assertThrows(IllegalArgumentException.class, () -> board.getLetterAt(new BoardPos(7, 7)));
    }

    @Test
    void hasTileAt() {
        Board board = new Board();

        assertFalse(board.hasTileAt(new BoardPos(7, 7)));
        board.setTile(new BoardPos(7, 7), Tile.E);
        assertTrue(board.hasTileAt(new BoardPos(7, 7)));

        assertFalse(board.hasTileAt(new BoardPos(7, 8)));
        board.setTile(new BoardPos(7, 8), Tile.A);
        assertTrue(board.hasTileAt(new BoardPos(7, 8)));

        assertFalse(board.hasTileAt(new BoardPos(7, 9)));
        board.setTile(new BoardPos(7, 9), Tile.D);
        assertTrue(board.hasTileAt(new BoardPos(7, 9)));
    }

    @Test
    void getModiferAt() {
        Board board = new Board();

        assertEquals(Square.Modifier.STAR, board.getModiferAt(new BoardPos(7, 7)));

        assertEquals(Square.Modifier.TRIPLE_WORD, board.getModiferAt(new BoardPos(0, 0)));
        assertEquals(Square.Modifier.TRIPLE_WORD, board.getModiferAt(new BoardPos(0, 14)));

        assertEquals(Square.Modifier.NORMAL, board.getModiferAt(new BoardPos(1, 14)));
    }

    @Test
    void applyWordPlacement() throws BadWordPlacementException {
        FakePool pool = new FakePool();
        pool.add(Tile.H);
        pool.add(Tile.E);
        pool.add(Tile.L);
        pool.add(Tile.L);
        pool.add(Tile.O);
        pool.add(Tile.X);
        pool.add(Tile.K);

        Board board = new Board();

        Player player = new Player("Bob");

        WordPlacement wordPlacement =
                new WordPlacement(new BoardPos(7, 7), WordPlacement.Direction.HORIZONTAL, "hello");

        assertThrows(
                BadWordPlacementException.class,
                () -> board.applyWordPlacement(player, wordPlacement));

        player.getFrame().refill(pool);
        board.applyWordPlacement(player, wordPlacement);

        assertEquals('H', board.getLetterAt(new BoardPos(7, 7)));
        assertEquals('E', board.getLetterAt(new BoardPos(7, 8)));
        assertEquals('L', board.getLetterAt(new BoardPos(7, 9)));
        assertEquals('L', board.getLetterAt(new BoardPos(7, 10)));
        assertEquals('O', board.getLetterAt(new BoardPos(7, 11)));

        assertThrows(
                BadWordPlacementException.class,
                () -> board.applyWordPlacement(player, wordPlacement));
    }
}
