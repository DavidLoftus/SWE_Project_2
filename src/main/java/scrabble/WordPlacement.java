package scrabble;

public class WordPlacement {

    public enum Direction {
        HORIZONTAL,
        VERTICAL
    }

    private BoardPos startPos;
    private Direction direction;
    private String word;

    public WordPlacement(BoardPos startPos, Direction direction, String word) {
        this.startPos = startPos;
        this.direction = direction;
        this.word = word.toUpperCase();

        if (direction == Direction.HORIZONTAL) {
            if (startPos.getColumn() + length() > 15) {
                throw new IllegalArgumentException("Word extends outside bounds of Board");
            }
        } else {
            if (startPos.getRow() + length() > 15) {
                throw new IllegalArgumentException("Word extends outside bounds of Board");
            }
        }
    }

    /** @return the length of the String word */
    public int length() {
        return word.length();
    }

    /**
     * @param i the index of the letter
     * @return the row in which selected letter is in
     */
    public BoardPos getPositionAt(int i) {
        if (i < 0 || i >= length()) {
            throw new IndexOutOfBoundsException();
        }
        if (direction == Direction.HORIZONTAL) {
            return new BoardPos(startPos.getRow(), startPos.getColumn() + i);
        } else {
            return new BoardPos(startPos.getRow() + i, startPos.getColumn());
        }
    }

    /**
     * @param i the index of the letter in the word
     * @return a character of the letter
     */
    public char getLetterAt(int i) {
        return word.charAt(i);
    }

    private boolean hasTileAtIfValidPos(Board board, int i, int j) {
        if (i < 0 || j < 0 || i >= 15 || j >= 15) {
            return false;
        }
        return board.hasTileAt(new BoardPos(i, j));
    }

    public boolean isConnectedToExistingTile(Board board) {
        for (int i = 0; i < length(); ++i) {
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
        for (int i = 0; i < length(); ++i) {
            BoardPos pos = getPositionAt(i);

            if (board.getModiferAt(pos) == Square.Modifier.STAR) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WordPlacement{");
        sb.append("[").append(startPos);
        sb.append("], ").append(direction);
        sb.append(", '").append(word).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
