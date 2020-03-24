package scrabble;

public class WordPlacement extends WordRange {

    private String word;

    public WordPlacement(BoardPos startPos, Direction direction, String word) {
        super(startPos, direction, word.length());
        this.word = word.toUpperCase();
    }

    /** @return the length of the String word */
    public int length() {
        return word.length();
    }

    /**
     * @param i the index of the letter in the word
     * @return a character of the letter
     */
    public char getLetterAt(int i) {
        return word.charAt(i);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WordPlacement{");
        sb.append("[").append(getStartPos());
        sb.append("], ").append(getDirection());
        sb.append(", '").append(word).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
