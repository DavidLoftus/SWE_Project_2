package scrabble;

public class Square {
    enum Modifier {
        DOUBLE_WORD,
        DOUBLE_LETTER,
        TRIPLE_WORD,
        TRIPLE_LETTER,
        NORMAL,
        STAR
    };

    private Tile tile = null;
    private Modifier mod;
    private char letter = 0;

    public Square(Modifier mod) {
        this.mod = mod;
    }

    public Tile getTile() {
        return tile;
    }

    public void setBlankTile(char letter) {
        this.tile = Tile.BLANK;
        this.letter = letter;
    }

    public void setTile(Tile tile) {
        if (tile == Tile.BLANK) {
            throw new IllegalArgumentException();
        }
        this.tile = tile;
        this.letter = tile.getSymbol();
    }

    public Modifier getModifier() {
        return mod;
    }

    public char getLetter() {
        return letter;
    }

    public boolean isEmpty() {
        return tile == null;
    }
}
