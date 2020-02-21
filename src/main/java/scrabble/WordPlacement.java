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

        if (i < 0 || i > length()) {
            throw new IndexOutOfBoundsException("Word size error");
        }

        if (direction == Direction.VERTICAL) {
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

        if (i < 0 || i > length()) {
            throw new IndexOutOfBoundsException("Word size error");
        }

        if (direction == Direction.HORIZONTAL) {
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

    public boolean checkForSurrondingNeighbours() {
        return true;
    }

    public boolean isConnectedToExistingTile() {
        return true;
    }
}
