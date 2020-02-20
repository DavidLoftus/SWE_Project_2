package scrabble;

public class WordPlacement {

    public String word = "";
    public int startI;
    public int startJ;

    public enum Direction {
        H("Horizontal"),
        V("Vertical");

        private final String dir;

        private Direction(String dir) {
            this.dir = dir;
        }
    }

    public int length() {
        return word.length();
    }

    public int getRowForLetter(int i) {
        if (i > 15 || i < 0) {
            throw new IllegalArgumentException("Out of bounds");
        }

        return i;
    }

    public int getColumnForLetter(int i) {
        if (i > 15 || i < 0) {
            throw new IllegalArgumentException("Out of bounds");
        }

        return i;
    }

    public char getLetterAt(int i) {
        return word.charAt(i);
    }

    public void Move(Direction direction, int startI, int startJ) {
        String word = this.word;
        this.startI = startI;
        this.startJ = startJ;

        if (direction == Direction.H) {
            // Horizontal move
        }

        if (direction == Direction.V) {
            // Vertical move
        }
    }
}
