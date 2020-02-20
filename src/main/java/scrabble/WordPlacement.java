package scrabble;

public class WordPlacement {

    public String word = "";
    public int start;
    public int end;
    public int direction;

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
        return word.toUpperCase().charAt(i);
    }

}
