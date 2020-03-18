package scrabble;

import java.util.Objects;

/**
 * BoardPos represents a single cell in the scrabble {@link Board}. This simplifies the methods as
 * it avoids having to pass around a row,column pair each time.
 *
 * <p>BoardPos objects are guaranteed to be within bounds, i.e. if invalid position is given to
 * constructor, an exception is thrown.
 */
public class BoardPos {
    private int row;
    private int column;

    /**
     * Constructs a new BoardPos with given row,column pair
     *
     * @param row the index going down the board
     * @param column the index going across the board (left to right)
     * @throws IllegalArgumentException if row or column is not within bounds [0, 15).
     */
    public BoardPos(int row, int column) {
        this.row = row;
        this.column = column;

        if (row < 0 || row >= 15) {
            throw new IllegalArgumentException("row outside bounds of Board");
        } else if (column < 0 || column >= 15) {
            throw new IllegalArgumentException("column outside bounds of Board");
        }
    }

    /** @return The row of the given BoardPos */
    public int getRow() {
        return row;
    }

    /** @return The row of the given BoardPos */
    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return String.format("%c%d", 'A' + getRow(), getColumn());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardPos boardPos = (BoardPos) o;
        return row == boardPos.row && column == boardPos.column;
    }

    public BoardPos valueOf(String str) {
        char charI = str.charAt(0);
        if (!Character.isAlphabetic(charI) || !Character.isUpperCase(charI) || charI > 'O') {
            throw new IllegalArgumentException("First character must be a letter between A to O");
        }
        int i = charI - 'A';
        int j = Integer.parseInt(str.substring(1));

        return new BoardPos(i, j);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
