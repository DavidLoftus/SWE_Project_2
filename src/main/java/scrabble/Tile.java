package scrabble;

/**
 * Tile represents an unplaced tile inside the {@link scrabble.Pool}
 *
 * <p>This enum is not appropriate for placed tiles, as it does not associate a symbol with the
 * wildcard BLANK enum.
 */
public enum Tile {
    BLANK(0, '?', 2),
    A(1, 'A', 9),
    B(3, 'B', 2),
    C(3, 'C', 2),
    D(2, 'D', 4),
    E(1, 'E', 12),
    F(4, 'F', 2),
    G(2, 'G', 3),
    H(4, 'H', 2),
    I(1, 'I', 9),
    J(8, 'J', 1),
    K(5, 'K', 1),
    L(1, 'L', 4),
    M(3, 'M', 2),
    N(1, 'N', 6),
    O(1, 'O', 8),
    P(3, 'P', 2),
    Q(10, 'Q', 1),
    R(1, 'R', 6),
    S(1, 'S', 4),
    T(1, 'T', 6),
    U(1, 'U', 4),
    V(4, 'V', 2),
    W(4, 'W', 2),
    X(8, 'X', 1),
    Y(4, 'Y', 2),
    Z(10, 'Z', 1);

    private int value;
    private char symbol;
    private int startingCount;

    Tile(int value, char symbol, int startingCount) {
        this.value = value;
        this.symbol = symbol;
        this.startingCount = startingCount;
    }

    /**
     * Gets the value associated with this tile. This is the score given to the player when they use
     * a tile of this type.
     *
     * @return the value of <code>this</code>
     */
    public int getValue() {
        return value;
    }

    /**
     * Gets the character associated with this tile. This is either an uppercase character or a
     * <code>'?'</code> in the case of the BLANK tile.
     *
     * @return the symbol for <code>this</code>
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     * Gets the starting number of this kind of tile that should be placed in the {@link
     * scrabble.Pool} at the start of a game.
     *
     * @return how many of this tile should be put in the pool
     */
    public int getStartingCount() {
        return startingCount;
    }

    public static Tile parseTile(char tileChar) {
        if (Character.isAlphabetic(tileChar)) {
            return values()[Character.toLowerCase(tileChar) - 'a' + 1];
        } else if (tileChar == '?') {
            return BLANK;
        } else {
            throw new IllegalArgumentException("Bad Tile character: " + tileChar);
        }
    }

    public static Tile parseTile(String tileStr) {
        if (tileStr.length() != 1) {
            throw new IllegalArgumentException("Bad Tile string: " + tileStr);
        }
        return parseTile(tileStr.charAt(0));
    }
}
