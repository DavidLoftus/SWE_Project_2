package scrabble;

import java.util.ArrayList;
import java.util.List;

public class Frame {

    private List<Tile> tiles = new ArrayList<>();

    public boolean hasTile(Tile letter) {
        for (Tile i : tiles) {
            if (i == letter) {
                return true;
            }
        }
        return false;
    }

    public void removeTile(Tile letter) {
        if (tiles.contains(letter)) {
            tiles.remove(letter);
        }
        else throw new IllegalArgumentException(letter.toString() + " does not exist in your frame.");
    }

    public void refill(Pool pool) {
        while (tiles.size() < 7){
            Tile newTile = pool.takeTile();
            tiles.add(newTile);
        }
    }

    public boolean isEmpty(){
        return tiles.isEmpty();
    }

    public String toString() {
        // TODO: implement code
        return ("Your frame consists of: ");
    }
}
