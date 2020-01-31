package scrabble;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Frame {

    private List<Tile> tiles = new ArrayList<>();

    /**
     * Check: if a letter is in the frame
     */
    public boolean hasTile(Tile letter) {
        for (Tile i : tiles) {
            if (i == letter) return true;
        }
        return false;
    }

    /**
     * Access: letters/tiles in the frame
     */
    public List<Tile> getTiles() {
        return tiles;
    }

    /**
     * Remove: letters/tiles in the frame
     */
    public void removeTile(Tile letter) {
        if (tiles.contains(letter)) {
            tiles.remove(letter);
            System.out.println(letter.toString() + " has been removed");
        }
        else throw new IllegalArgumentException(letter.toString() + " does not exist in your frame.");
    }

    /**
     * Refill: the frame from the pool
     */
    public void refill(Pool pool){
        // TODO: Calculations may require additional testing
        int i = 7 - tiles.size();
        while (i > 0) {
            Tile newTile = pool.takeTile();
            tiles.addAll(Arrays.asList(newTile));
            i--;
        }
    }

    /**
     * Check: if the frame is empty
     */
    public boolean isEmpty() {
        return tiles.isEmpty();
    }
}
