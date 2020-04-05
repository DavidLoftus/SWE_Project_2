package scrabble;

import java.util.*;

/**
 * Frame acts as the tray of 7 tiles given to each player. Frames can be filled and refilled using
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
     * Checks if a frame is not empty/contains at least 1 tile.
     *
     * @param tiles the list of tiles in the frame.
     * @return true if the the list of tiles is not empty (or equal 0).
     */
    public boolean hasTiles(List<Tile> tiles) {
        Map<Tile, Integer> map = getTileCounts();
        for (Tile tile : tiles) {
            int count = map.getOrDefault(tile, 0);
            if (count == 0) {
                return false;
            }
            map.put(tile, count - 1);
        }
        return true;
    }

    /**
     * Counts the tiles currently present in a frame
     *
     * @return the number of tiles in a frame
     */
    public Map<Tile, Integer> getTileCounts() {
        Map<Tile, Integer> map = new EnumMap<Tile, Integer>(Tile.class);
        for (Tile tile : tiles) {
            map.put(tile, map.getOrDefault(tile, 0) + 1);
        }
        return map;
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
     * @return letter if it was in the Frame
     * @throws NoSuchElementException if letter was not in the Frame
     */
    public Tile removeTile(Tile letter) {
        if (!tiles.contains(letter)) {
            throw new NoSuchElementException(letter.toString() + " not found in frame");
        }
        tiles.remove(letter);
        return letter;
    }

    /**
     * Removes a list of the tiles from the frame
     *
     * @param letters the list of tiles in the frame.
     */
    public void removeTiles(List<Tile> letters) {
        for (Tile tiles : letters) {
            removeTile(tiles);
        }
    }

    /**
     * Refills frame with tiles from given {@link scrabble.Pool}. After this frame should have
     * exactly 7 tiles, unless the Pool was emptied.
     *
     * @param pool The pool to take tiles from to refill the frame.
     */
    public void refill(Pool pool) {
        while (!pool.isEmpty() && tiles.size() < 7) {
            Tile newTile = pool.takeTile();
            tiles.add(newTile);
        }
    }

    /**
     * Adds a tile to the frame if there is space
     * @param tile the tile to add to the frame
     * @throws IllegalArgumentException if there is no space for tile.
     */
    public void addTile(Tile tile) {
        if (tiles.size() >= 7) {
            throw new IllegalArgumentException();
        }
        tiles.add(tile);
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

    /**
     * Returns the tiles from the frame that can be used to place word on the board. This method, if
     * successful will modify the contents of frame.
     *
     * @param neededTiles the tiles needed to place the word on the board
     * @return If all needed tiles were found returns a list containing those needed tiles.
     *     Otherwise returns null.
     */
    List<Tile> getTilesToPlace(List<Tile> neededTiles) {
        if (neededTiles.size() > 7) {
            return null;
        }
        List<Tile> ret = new ArrayList<>(neededTiles.size());

        for (Tile tile : neededTiles) {
            if (hasTile(tile)) {
                ret.add(removeTile(tile));
            } else if (hasTile(Tile.BLANK)) {
                ret.add(removeTile(Tile.BLANK));
            } else {
                // Re-add tiles back to frame if failed
                tiles.addAll(ret);
                return null;
            }
        }

        return ret;
    }

    // For debugging only
    public void setAll(Tile... tile) {
        tiles.clear();
        tiles.addAll(Arrays.asList(tile));
    }
}
