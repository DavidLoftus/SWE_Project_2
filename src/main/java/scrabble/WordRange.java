package scrabble;

import java.util.Iterator;

/// TODO: This would essentially be copypaste of WordPlacement, fix up and update WordPlacement to
// use WordRange
// ^ might involve fixing tests
public class WordRange implements Iterable<BoardPos> {

    public enum Direction {
        HORIZONTAL,
        VERTICAL
    }

    private BoardPos startPos;
    private Direction direction;
    private int length;

    public WordRange(BoardPos startPos, Direction direction, int lenght) {
        this.startPos = startPos;
        this.direction = direction;
        this.length = lenght;

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
     * @param i the index of the letter
     * @return the row in which selected letter is in
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

    public int getLength() {
        return length;
    }

    private boolean hasTileAtIfValidPos(Board board, int i, int j) {
        if (i < 0 || j < 0 || i >= 15 || j >= 15) {
            return false;
        }
        return board.hasTileAt(new BoardPos(i, j));
    }

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

    public boolean isPlacedAtStar(Board board) {
        for (int i = 0; i < length; ++i) {
            BoardPos pos = getPositionAt(i);

            if (board.getModiferAt(pos) == Square.Modifier.STAR) {
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
