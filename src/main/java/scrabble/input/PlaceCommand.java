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
        int startPosI = start.charAt(0) - 'A';
        int startPosJ = Integer.parseInt(start.substring(1));

        BoardPos bp = new BoardPos(startPosI, startPosJ);

        if (!string.hasMoreTokens()) {
            return null;
        }
        String direction = string.nextToken();

        if (!string.hasMoreTokens()) {
            return null;
        }
        String word = string.nextToken();

        WordPlacement wp;
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
