package scrabble.input;

import java.util.StringTokenizer;
import scrabble.BoardPos;
import scrabble.WordPlacement;

public class PlaceCommand implements InputCommand {

    public WordPlacement wordPlacement;

    public PlaceCommand(WordPlacement wordPlacement) {
        this.wordPlacement = wordPlacement;
    }

    /**
     * The command to place a bunch of tiles (making a word) from a player's frame.
     *
     * @param str should be in the form of the starting position of the word to be placed, its
     *     direction and the word
     * @return a new PlaceCommand
     */
    public static PlaceCommand valueOf(String str) {
        str = str.toUpperCase();
        StringTokenizer string = new StringTokenizer(str, " ");

        if (!string.hasMoreTokens()) {
            return null;
        }

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
