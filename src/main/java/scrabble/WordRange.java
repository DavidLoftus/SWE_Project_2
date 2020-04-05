package scrabble;

import java.util.Iterator;

public class WordRange implements Iterable<BoardPos> {

    public enum Direction {
        HORIZONTAL,
        VERTICAL
    }

    private BoardPos startPos;
    private Direction direction;
    private int length;

    /**
     * WordRange constructor.
     *
     * @param startPos the position of the first letter of the word.
     * @param direction the direction of the word placed.
     * @param length the length of the word.
     */
    public WordRange(BoardPos startPos, Direction direction, int length) {
        this.startPos = startPos;
        this.direction = direction;
        this.length = length;

        if (direction == Direction.HORIZONTAL) {
            if (startPos.getColumn() + length > 15) {
                throw new IllegalArgumentException("Word extends outside bounds of Board");
            }
        } else {
            if (startPos.getRow() + length > 15) {
                throw new IllegalArgumentException("Word extends outside bounds of Board");
            }
        }
    }

    public BoardPos getStartPos() {
        return this.startPos;
    }

    public Direction getDirection() {
        return this.direction;
    }

    /**
     * @param i the index of the letter.
     * @return the row in which selected letter is in.
     */
    public BoardPos getPositionAt(int i) {
        if (i < 0 || i >= length) {
            throw new IndexOutOfBoundsException();
        }
        if (direction == Direction.HORIZONTAL) {
            return new BoardPos(startPos.getRow(), startPos.getColumn() + i);
        } else {
            return new BoardPos(startPos.getRow() + i, startPos.getColumn());
        }
    }

    /** @return the length of the word range. */
    public int getLength() {
        return length;
    }

    /**
     * Checks if a tile is in a valid position.
     *
     * @param board the board of the game.
     * @param i the row position of the tile.
     * @param j the column position of the tile.
     * @return True if the tile is in a valid position.
     */
    private boolean hasTileAtIfValidPos(Board board, int i, int j) {
        if (i < 0 || j < 0 || i >= 15 || j >= 15) {
            return false;
        }
        return board.hasTileAt(new BoardPos(i, j));
    }

    /**
     * @param board the board of the game.
     * @return true if there's a tile above or below.
     */
    public boolean isConnectedToExistingTile(Board board) {
        for (int i = 0; i < length; ++i) {
            BoardPos pos = getPositionAt(i);

            if (board.hasTileAt(pos)) {
                return true;
            }

            int row = pos.getRow(), column = pos.getColumn();

            if (hasTileAtIfValidPos(board, row - 1, column)
                    || hasTileAtIfValidPos(board, row + 1, column)
                    || hasTileAtIfValidPos(board, row, column - 1)
                    || hasTileAtIfValidPos(board, row, column + 1)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param board the board of the game.
     * @return true if the word is placed at the star.
     */
    public boolean isPlacedAtStar(Board board) {
        for (int i = 0; i < length; ++i) {
            BoardPos pos = getPositionAt(i);

            if (board.getModifierAt(pos) == Square.Modifier.STAR) {
                return true;
            }
        }
        return false;
    }

    public Iterator<BoardPos> iterator() {

        return new Iterator<BoardPos>() {

            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < length;
            }

            @Override
            public BoardPos next() {
                BoardPos pos = getPositionAt(i);
                i++;
                return pos;
            }
        };
    }
}
