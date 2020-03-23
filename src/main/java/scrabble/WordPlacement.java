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

        new WordRange(startPos, direction, word.length());
    }
    
    /** @return the length of the String word */
    public int length() {
        return word.length();
    }
    
    public BoardPos getStartPos() {
    	return this.startPos;
    }
    
    public Direction getDirection() {
    	return this.direction;
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
        sb.append("[").append(startPos);
        sb.append("], ").append(direction);
        sb.append(", '").append(word).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
