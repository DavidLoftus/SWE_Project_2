package scrabble;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Frame acts as the tray of 7 tiles given to each player. Frames can be filled & refilled using
 * {@link Frame#refill(Pool)} method. You can check if the frame contains certain tiles and
 * additionally remove specified tiles from the frame.
 */
public class Frame {

    private List<Tile> tiles = new ArrayList<>();

    /**
     * Initializes Frame with an empty list of tiles. Must be filled using {@link
     * Frame#refill(Pool)} method.
     */
    public Frame() {}

    /**
     * Checks if Frame has given tile
     *
     * @param letter the {@link Tile} search for
     * @return true if frame contains letter otherwise false
     */
    public boolean hasTile(Tile letter) {
        for (Tile i : tiles) {
            if (i == letter) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the list of tiles. This is an immutable view of the list. You may not use this method to
     * modify the contents of the frame.
     *
     * @return the list of tiles in a frame
     */
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
        } else {
            throw new NoSuchElementException(letter.toString() + " not found in frame");
        }
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

    /**
     * Checks whether the frame is empty.
     *
     * @return true if the pool is empty otherwise false
     */
    public boolean isEmpty() {
        return tiles.isEmpty();
    }

    /**
     * Converts the frame to string format. String format looks like [A, B, C, D]
     *
     * @return the list of tiles in the frame as String
     */
    @Override
    public String toString() {
        return getTiles().toString();
    }
}
