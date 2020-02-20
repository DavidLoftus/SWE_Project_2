package scrabble;

public class WordPlacement {

    public String word = "";
    public int startI;
    public int startJ;

    public enum Direction {
        HORIZONTAL,
        DIRECTION
    }

    public int length() {
        return word.length();
    }

    public int getRowForLetter(int i) {
        // TODO : should be a constant when word is placed horizontally (maybe assumed)
        int row = 0;

        if (i > word.length()) {
            throw new IllegalArgumentException("Word size error");
        }

        row = startI + i - 1;

        if (row > 15 || row < 0) {
            throw new IllegalArgumentException("Out of bounds");
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
