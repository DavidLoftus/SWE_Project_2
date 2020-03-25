package scrabble.input;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import scrabble.Tile;

/** ExhangeCommand is the list of tiles that the player wishes to exchange for different tiles. */
public class ExchangeCommand implements InputCommand {

    public List<Tile> tiles;

    public ExchangeCommand(List<Tile> tiles) {
        this.tiles = tiles;
    }

    @Override
    public boolean usesTurn() {
        return true;
    }

    /**
     * @param str from user input
     * @return this list of tiles the user wishes to exchange
     */
    public static ExchangeCommand valueOf(String str) {
        str = str.toUpperCase();
        StringTokenizer string = new StringTokenizer(str, " ");
        if (!string.hasMoreTokens()) {
            return null;
        }
        if (!string.nextToken().equals("EXCHANGE")) {
            return null;
        }

        if (!string.hasMoreTokens()) {
            return null;
        }

        List<Tile> tilesToReturn = new ArrayList<>();
        char[] tileList = string.nextToken().toCharArray();

        for (int i = 0; i < tileList.length; i++) {
            for (Tile t : Tile.values()) {
                if (tileList[i] == t.getLetter()) {
                    tilesToReturn.add(t);
                }
            }
        }
        return new ExchangeCommand(tilesToReturn);
    }
}
