package scrabble;

public class WordPlacement {

    public enum Direction {
        HORIZONTAL,
        DIRECTION
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

    public int length() {
        return word.length();
    }

    public int getRowForLetter(int i) {
        // TODO : should be a constant when word is placed horizontally (maybe assumed)
        int row = 0;

        if (i > word.length()) {
            throw new IndexOutOfBoundsException("Word size error");
        }

        row = startI + i - 1;

        if (row >= 15 || row < 0) {
            throw new IndexOutOfBoundsException("Word goes off edge of board");
        }

        return row;
    }

    public int getColumnForLetter(int i) {
        // TODO : should be a constant when word is placed vertically (maybe assumed)
        int col = 0;

        if (i > word.length()) {
            throw new IllegalArgumentException("Word size error");
        }

        col = startJ + i - 1;

        if (col > 15 || col < 0) {
            throw new IllegalArgumentException("Out of bounds");
        }

        return col;
    }

    public char getLetterAt(int i) {
        return word.charAt(i);
    }
}
