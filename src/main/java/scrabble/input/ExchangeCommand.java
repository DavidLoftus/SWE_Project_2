package scrabble.input;

import java.util.List;
import java.util.StringTokenizer;

import scrabble.Tile;

public class ExchangeCommand implements InputCommand {

    public List<Tile> tiles;

    @Override
    public boolean usesTurn() {
        return true;
    }

    static ExchangeCommand valueOf(String str) {
    	str.toUpperCase();//not sure if input has to be in upper case to be accepted or???
    	StringTokenizer string = new StringTokenizer(str, " ");
    	if (!string.nextToken().equals("EXCHANGE")) {
    		return null;
    	}
    	List<Tile> tilesToReturn = null;
    	char[] tileList = string.nextToken().toCharArray();
    	for(int i = 0; i < tileList.length; i++) {
    		for(Tile t :  Tile.values()) {
    			if(tileList[i] == t.getLetter()) {
    				tilesToReturn.add(t);
    			}
    		}
    	}
        return (ExchangeCommand) tilesToReturn;
    }
}
