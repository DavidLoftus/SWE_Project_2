package scrabble;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Frame {

    private List<Tile> tiles = new ArrayList<>();

    /**
     * @param letter or tile
     * @return true if the param letter is equal to the value in i
     */
    public boolean hasTile(Tile letter) {
        for (Tile i : tiles) {
            if (i == letter) return true;
        }
        return false;
    }

    /** @return the list of tiles in a frame */
    public List<Tile> getTiles() {
        return tiles;
    }

    /** Remove a specific letter/tile in the frame */
    public void removeTile(Tile letter) {
        if (tiles.contains(letter)) {
            tiles.remove(letter);
            System.out.println(letter.toString() + " has been removed");
        } else throw new NoSuchElementException(letter.toString() + " is not exist in your frame.");
    }

    /** Refill the frame with tiles/letters from the Pool */
    public void refill(Pool pool) {
        int i = 7 - tiles.size();
        while (i > 0) {
            Tile newTile = pool.takeTile();
            tiles.add(newTile);
            i--;
        }
    }

    /** @return whether the pool is empty or not */
    public boolean isEmpty() {
        return tiles.isEmpty();
    }

    /** @return the list of tiles in the frame as String */
    public String toString() {
        return getTiles().toString();
    }
}
