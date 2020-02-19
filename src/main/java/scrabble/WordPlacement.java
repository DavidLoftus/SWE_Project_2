package scrabble;

public class WordPlacement {

    // placeholder
    public Tile letterToPlace(Tile tile) {
        return tile;
    }

    // placeholder
    public boolean checkTile(Player player, Tile tile) {
        if (player.getFrame().getTiles().contains(letterToPlace(tile))) {
            return true;
        } else return false;
    }

    /*public void placeLetter(Tile tile, int i, int j) {
        if (checkTile() == true){  }
    }*/

}
