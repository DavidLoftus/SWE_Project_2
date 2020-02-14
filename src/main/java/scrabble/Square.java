package scrabble;

public class Square {
    enum Modifier {
        DOUBLLE_WORD,
        DOUBLE_LETTER,
        TRIPLE_WORD,
        TRIPLE_LETTER,
        NORMAL,
        STAR
    };

    private Tile tile;
    private Modifier mod;
    private char letter;

    public Square(Tile tile, Modifier mod, char letter) {
        this.tile = tile;
        this.mod = mod;
        this.letter = letter;
    }

    public Tile getTile() {
        return tile;
    }

    public Modifier getModifier() {
        return mod;
    }

    public char getLetter() {
        return letter;
    }
}
