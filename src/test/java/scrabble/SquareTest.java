package scrabble;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class SquareTest {

    @Test
    void setBlankTile() {
        Square square = new Square(Square.Modifier.NORMAL);
        square.setBlankTile('Y');
        assertEquals('Y', square.getLetter());
    }

    @Test
    void setTile() {
        Square square = new Square(Square.Modifier.NORMAL);
        assertThrows(IllegalArgumentException.class, () -> square.setTile(Tile.BLANK));
        square.setTile(Tile.H);
        assertEquals('H', square.getLetter());
        assertEquals(Square.Modifier.NORMAL, square.getModifier());
    }

    @Test
    void getTile() {
        Square square = new Square(Square.Modifier.NORMAL);
        square.setTile(Tile.A);
        assertEquals(Tile.A, square.getTile());
    }

    @Test
    void getLetter() {
        Square square = new Square(Square.Modifier.NORMAL);
        square.setTile(Tile.A);
        assertEquals('A', square.getLetter());
    }

    @Test
    void isEmpty() {
        Square square = new Square(Square.Modifier.NORMAL);
        assertEquals(true, square.isEmpty());
        square.setTile(Tile.E);
        assertEquals(false, square.isEmpty());
    }

    @Test
    void getModifier() {
        Square square = new Square(Square.Modifier.NORMAL);
        assertEquals(Square.Modifier.NORMAL, square.getModifier());
    }

    @Test
    void testtoString() {
        Square square = new Square(Square.Modifier.NORMAL);
        assertEquals("  ", square.toString());
        Square square5 = new Square(Square.Modifier.STAR);
        assertEquals("* ", square5.toString());
        Square square1 = new Square(Square.Modifier.DOUBLE_WORD);
        assertEquals("DW", square1.toString());
        Square square2 = new Square(Square.Modifier.DOUBLE_LETTER);
        assertEquals("DL", square2.toString());
        Square square3 = new Square(Square.Modifier.TRIPLE_WORD);
        assertEquals("TW", square3.toString());
        Square square4 = new Square(Square.Modifier.TRIPLE_LETTER);
        assertEquals("TL", square4.toString());
        square.setTile(Tile.A);
        assertEquals("A ", square.toString());
        square2.setTile(Tile.S);
        assertEquals("S ", square2.toString());
        square4.setTile(Tile.P);
        assertEquals("P ", square4.toString());
    }
}
