package scrabble;

public class Square {
    public enum Modifier {
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

    /** @return the tile */
    public Tile getTile() {
        return tile;
    }

    /** @param letter the letter which will replace the blank tile. */
    public void setBlankTile(char letter) {
        this.tile = Tile.BLANK;
        this.letter = letter;
    }

    /** @param tile mutator method to set tile to a specific letter. */
    public void setTile(Tile tile) {
        if (tile == Tile.BLANK) {
            throw new IllegalArgumentException();
        }
        this.tile = tile;
        this.letter = tile.getLetter();
    }

    /** @return accessor method to get the modifier. */
    public Modifier getModifier() {
        return mod;
    }

    /** @return accessor method to get the letter. */
    public char getLetter() {
        return letter;
    }

    /** @return true or false if the tile is empty. */
    public boolean isEmpty() {
        return tile == null;
    }

    /** @return a string that indicates the modifier being used. */
    public String toString() {
        if (getTile() != null) {
            return getLetter() + " ";
        } else {
            switch (getModifier()) {
                case DOUBLE_WORD:
                    return "DW";
                case DOUBLE_LETTER:
                    return "DL";
                case TRIPLE_WORD:
                    return "TW";
                case TRIPLE_LETTER:
                    return "TL";
                case NORMAL:
                    return "  ";
                case STAR:
                    return "* ";
            }
        }
        return null;
    }
}
