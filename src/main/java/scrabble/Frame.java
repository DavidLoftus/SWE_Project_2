package scrabble;

import java.util.List;

public class Frame {

    private List<Tile> tiles;

    public boolean hasTile(Tile letter)
    {
        for (Tile i : tiles) {
            if (i == letter) {
                return true;
            }
            //else return false;
        }
        return false;
    }

    public void removeTile(Tile letter)
    {
        if (tiles.contains(letter)) {
            tiles.remove(letter);
        }
        else throw new IllegalArgumentException(letter.toString() + " does not exist in your frame.");
    }

    public void refill(Pool pool){
        int index = 0;
        while (index <= 7){
            // TODO: finish code
            Tile newTile = pool.takeTile();
        }
    }

    public boolean isEmpty(){
        return tiles.isEmpty();
    }

    public String toString(){
        // TODO: implement code
        return ("Your frame consists of: ");
    }
}
