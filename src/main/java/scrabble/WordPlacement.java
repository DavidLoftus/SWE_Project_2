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

        if (i > word.length()) {
            throw new IndexOutOfBoundsException("Word size error");
        }

        if (direction == Direction.HORIZONTAL) {
            row = startI;
        } else row = startI + i - 1;

        if (row >= 15 || row < 0) {
            throw new IndexOutOfBoundsException("Word goes off edge of board");
        }

        return row;
    }

    /**
     * @param i the index of the letter
     * @return the column in which the selected letter is in
     */
    public int getColumnForLetter(int i) {
        int col;

        if (i > word.length()) {
            throw new IllegalArgumentException("Word size error");
        }

        if (direction == Direction.VERTICAL) {
            col = startJ;
        } else col = startJ + i - 1;

        if (col > 15 || col < 0) {
            throw new IllegalArgumentException("Out of bounds");
        }

        return col;
    }

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
