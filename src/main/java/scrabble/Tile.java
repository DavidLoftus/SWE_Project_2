package scrabble;

public enum Tile {
    BLANK,
    A,
    B,
    C,
    D,
    E,
    F,
    G,
    H,
    I,
    J,
    K,
    L,
    M,
    N,
    O,
    P,
    Q,
    R,
    S,
    T,
    U,
    V,
    W,
    X,
    Y,
    Z;

    /**
     * Gets the value associated with this tile.
     * This is the score given to the player when they use a tile of this type.
     *
     * @return the value of <code>this</code>
     */
    public int getValue() {
        // TODO: implement code
        throw new UnsupportedOperationException();
    }

    /**
     * Gets the character associated with this tile.
     * This is either an uppercase character or a <code>'?'</code> in the case of the BLANK tile.
     *
     * @return the symbol for <code>this</code>
     */
    public char getSymbol() {
        // TODO: implement code
        throw new UnsupportedOperationException();
    }

    /**
     * Gets the starting number of this kind of tile that should be placed in the {@link scrabble.Pool} at the start of
     * a game.
     *
     * @return how many of this tile should be put in the pool
     */
    public int getDefaultStartingCount() {
        // TODO: implement code
        throw new UnsupportedOperationException();
    }
}
