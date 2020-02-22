package scrabble;

public class WordPlacement {

    public enum Direction {
        HORIZONTAL,
        VERTICAL
    }

    private int startI;
    private int startJ;
    private Direction direction;
    private String word;

    public WordPlacement(int startI, int startJ, Direction direction, String word) {
        this.startI = startI;
        this.startJ = startJ;
        this.direction = direction;
        this.word = word;
    }

    /** @return the length of the String word */
    public int length() {
        return word.length();
    }

    /**
     * @param i the index of the letter
     * @return the row in which selected letter is in
     */
    public int getRowForLetter(int i) {
        int row;

        if (i < 0 || i >= length()) {
            throw new IndexOutOfBoundsException("Word size error");
        }

        if (direction == Direction.HORIZONTAL) {
            row = startI;
        } else {
            row = startI + i;
        }

        if (row >= 15 || row < 0) {
            throw new IndexOutOfBoundsException("Word is out of bounds");
        }

        return row;
    }

    /**
     * @param i the index of the letter
     * @return the column in which the selected letter is in
     */
    public int getColumnForLetter(int i) {
        int col;

        if (i < 0 || i >= length()) {
            throw new IndexOutOfBoundsException("Word size error");
        }

        if (direction == Direction.VERTICAL) {
            col = startJ;
        } else {
            col = startJ + i;
        }

        if (col >= 15 || col < 0) {
            throw new IndexOutOfBoundsException("Word is out of bounds");
        }

        return col;
    }

    /**
     * @param i the index of the letter in the word
     * @return a character of the letter
     */
    public char getLetterAt(int i) {
        return word.charAt(i);
    }

    public boolean isConnectedToExistingTile(Board board) {
        for (int i = 0; i < length(); ++i) {
            int row = getRowForLetter(i);
            int column = getColumnForLetter(i);

            if (board.hasTileAt(row, column)) {
                return true;
            }
        }

        int rowOffset = 0, columnOffset = 0;
        if (direction == Direction.HORIZONTAL) {
            rowOffset = 1;

            if (board.hasTileAt(startI, startJ - 1) || board.hasTileAt(startI, startJ + length())) {
                return true;
            }
        } else {
            columnOffset = 1;

            if (board.hasTileAt(startI - 1, startJ) || board.hasTileAt(startI + length(), startJ)) {
                return true;
            }
        }

        for (int i = 0; i < length(); ++i) {
            int row = getRowForLetter(i);
            int column = getColumnForLetter(i);

            if (board.hasTileAt(row + rowOffset, column + columnOffset)) {
                return true;
            }
            if (board.hasTileAt(row - rowOffset, column - columnOffset)) {
                return true;
            }
        }

        return false;
    }

    public boolean isPlacedAtStar(Board board) {
        for (int i = 0; i < length(); ++i) {
            int row = getRowForLetter(i);
            int column = getColumnForLetter(i);

            if (board.getModiferAt(row, column) == Square.Modifier.STAR) {
                return true;
            }
        }
        return false;
    }
}
