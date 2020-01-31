package scrabble;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Frame {

    private List<Tile> tiles = new ArrayList<>();

    public boolean hasTile(Tile letter)
    {
        for (Tile i : tiles) {
            if (i == letter) return true;
        }
        return false;
    }

    public void removeTile(Tile letter)
    {
        if (tiles.contains(letter)) {
            tiles.remove(letter);
            System.out.println(letter.toString() + " has been removed");
        }
        else throw new IllegalArgumentException(letter.toString() + " does not exist in your frame.");
    }

    public void refill(Pool pool){
        // TODO: May need testing
        int i = 7 - tiles.size();
        while (i > 0) {
            Tile newTile = pool.takeTile();
            tiles.addAll(Arrays.asList(newTile));
            i--;
        }
    }

    public boolean isEmpty() {
        return tiles.isEmpty();
    }

    // Display frame
    public String toString(){
        // TODO: implement code
        return ("Your frame consists of: ");
    }
}
