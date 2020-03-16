package scrabble.input;

import java.util.StringTokenizer;
import scrabble.BoardPos;
import scrabble.WordPlacement;

public class PlaceCommand implements InputCommand {

    public WordPlacement wordPlacement;

    public PlaceCommand(WordPlacement wordPlacement) {
        this.wordPlacement = wordPlacement;
    }

    @Override
    public boolean usesTurn() {
        return true;
    }

    static PlaceCommand valueOf(String str) {
        str.toUpperCase(); // not sure if input has to be in upper case to be accepted or???
        StringTokenizer string = new StringTokenizer(str, " ");
        char[] start = string.nextToken().toCharArray();
        if ('A' < start[0] || start[0] > 'O') {
            return null;
        }
        // do the start postions go from 0-14 or 1-15? who knows ive said 0-14
        int startposi = start[0] - 65;
        int startposj;
        if (start.length > 2) {
            if (start.length > 3) {
                throw new IllegalArgumentException("Must be between 1 and 15.");
            } else {
                startposj = Character.getNumericValue(start[1]) * 10;
                startposj += Character.getNumericValue(start[2]) - 1;
            }
        } else {
            startposj = Character.getNumericValue(start[1]) - 1;
        }
        BoardPos bp = new BoardPos(startposi, startposj);
        String direction = string.nextToken();
        String word = string.nextToken();
        WordPlacement wp = null;
        if (direction.startsWith("A")) {
            wp = new WordPlacement(bp, WordPlacement.Direction.HORIZONTAL, word);
        } else if (direction.startsWith("D")) {
            wp = new WordPlacement(bp, WordPlacement.Direction.VERTICAL, word);
        }else {
        	return null;
        }
        return new PlaceCommand(wp);
    }
}
