package scrabble;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Frame {

    private List<Tile> tiles = new ArrayList<>();

    /**
     * @param letter the {@link Tile} search for
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

    /**
     * Removes specified tile from the frame.
     *
     * @param letter the letter to remove
     * @throws NoSuchElementException if letter is not in Frame
     */
    public void removeTile(Tile letter) {
        if (tiles.contains(letter)) {
            tiles.remove(letter);
            System.out.println(letter.toString() + " has been removed");
        } else throw new NoSuchElementException(letter.toString() + " is not exist in your frame.");
    }

    /**
     * Refills frame with tiles from given {@link scrabble.Pool}. After this frame should have
     * exactly 7 tiles, unless the Pool was emptied.
     *
     * @param pool The pool to take tiles from to refill the frame.
     */
    public void refill(Pool pool) {
        int i = 7 - tiles.size();
        while (i > 0) {
            // TODO: fix issue where pool can be empty when taking out tiles.
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
