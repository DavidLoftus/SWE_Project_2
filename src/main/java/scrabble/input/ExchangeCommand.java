package scrabble.input;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import scrabble.Tile;

public class ExchangeCommand implements InputCommand {

    public List<Tile> tiles;

    public ExchangeCommand(List<Tile> tiles) {
        this.tiles = tiles;
    }

    @Override
    public boolean usesTurn() {
        return true;
    }

    public static ExchangeCommand valueOf(String str) {
        str = str.toUpperCase(); // not sure if input has to be in upper case to be accepted or???
        StringTokenizer string = new StringTokenizer(str, " ");
        if (!string.nextToken().equals("EXCHANGE")) {
            return null;
        }
        List<Tile> tilesToReturn = new ArrayList<>();
        char[] tileList = string.nextToken().toCharArray();
        if (!string.hasMoreTokens()) {
            return null;
        }
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
