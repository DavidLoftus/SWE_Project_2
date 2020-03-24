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

    public static PlaceCommand valueOf(String str) {
        str = str.toUpperCase(); 
        StringTokenizer string = new StringTokenizer(str, " ");
        String start = string.nextToken();
        if (!Character.isAlphabetic(start.charAt(0))) {
            return null;
        }
        // do the start postions go from 0-14 or 1-15? who knows ive said 0-14
        int startposi = start.charAt(0);
        int startposj = Integer.parseInt(start.substring(1));
        
        BoardPos bp = new BoardPos(startposi, startposj);
        String direction = string.nextToken();
        String word = string.nextToken();
        WordPlacement wp = null;
        if (direction.startsWith("A")) {
            wp = new WordPlacement(bp, WordPlacement.Direction.HORIZONTAL, word);
        } else if (direction.startsWith("D")) {
            wp = new WordPlacement(bp, WordPlacement.Direction.VERTICAL, word);
        } else {
            return null;
        }
        return new PlaceCommand(wp);
    }
}
