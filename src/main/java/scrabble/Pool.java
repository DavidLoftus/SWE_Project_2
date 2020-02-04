package scrabble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Pool acts as a bag of {@link scrabble.Tile} that the players can take tiles from at random.
 *
 * <p>When instantiated, will fill itself with correct number of each tile.
 */
public class Pool {

    private List<Tile> tiles = new ArrayList<>();

    public Pool() {
        reset();
    }

    /** Resets pool to original state by refilling with correct number of each tile. */
    public void reset() {
        tiles.clear();
        for (Tile tile : Tile.values()) {
            int count = tile.getStartingCount();
            tiles.addAll(Collections.nCopies(count, tile));
        }
        Collections.shuffle(tiles);
    }

    /** @return the number of tiles currently in the pool */
    public int size() {
        return tiles.size();
    }

    /** @return whether the pool is empty or not */
    public boolean isEmpty() {
        return tiles.isEmpty();
    }

    /**
     * Takes a single {@link scrabble.Tile} from the pool at random. The size of the pool will
     * decrease by 1.
     *
     * @throws java.util.NoSuchElementException if pool is empty.
     * @return the tile taken from the pool
     */
    public Tile takeTile() {
        if (tiles.isEmpty()) {
            throw new NoSuchElementException("tried to take from empty pool");
        }
        int last = tiles.size() - 1;
        Tile tile = tiles.get(last);
        tiles.remove(last);
        return tile;
    }
}
