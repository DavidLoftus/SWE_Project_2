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

    public boolean isConnectedToExistingTile(WordPlacement wordplacement, Board board) {

        int row = wordplacement.getColumnForLetter(startJ);
        int column = wordplacement.getRowForLetter(startI);
        int noOfTiles = 0;
        if (direction == Direction.HORIZONTAL) {
            for (int i = column - 1; i < wordplacement.length() + 2; i++) {
                for (int j = row - 1; j < row + 1; j++) {
                    try {
                        board.getLetterAt(i, j);
                    } catch (IllegalArgumentException e) {
                        noOfTiles++;
                    }
                }
            }
        }
        if (direction == Direction.VERTICAL) {
            for (int i = row - 1; i < wordplacement.length() + 2; i++) {
                for (int j = column - 1; j < 2; j++) {
                    try {
                        board.getLetterAt(i, j);
                    } catch (IllegalArgumentException e) {
                        noOfTiles++;
                    }
                }
            }
        }
        if (noOfTiles == 0) {
            return false;
        }
        if (isPlacedAtStar(wordplacement, board) == false) {
            return false;
        }
        return true;
    }

    public boolean isPlacedAtStar(WordPlacement wordplacement, Board board) {
        int row = wordplacement.getColumnForLetter(startJ);
        int column = wordplacement.getRowForLetter(startI);
        if (direction == Direction.HORIZONTAL) {
            for (int i = column; i < wordplacement.length(); i++) {
                // TODO
            }
        }
        if (direction == Direction.VERTICAL) {
            for (int i = row; i < wordplacement.length(); i++) {
                // TODO
            }
        }
        return false;
    }
}
