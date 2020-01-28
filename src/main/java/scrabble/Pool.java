package scrabble;

/**
 * Pool acts as a bag of {@link scrabble.Tile} that the players can take tiles from at random.
 *
 * When instantiated, will fill itself with correct number of each tile.
 */
public class Pool {

    private List<Tile> tiles = new ArrayList<>();

    public Pool() {
        reset();
    }

    /**
     * Resets pool to original state by refilling with correct number of each tile.
     */
    public void reset() {
        // TODO: implement code
        throw new UnsupportedOperationException();
    }

    /**
     * @return the number of tiles currently in the pool
     */
    public int size() {
        return tiles.size();
    }

    /**
     * @return whether the pool is empty or not
     */
    public boolean isEmpty() {
        return tiles.isEmpty();
    }

    /**
     * Takes a single {@link scrabble.Tile} from the pool at random.
     * The size of the pool will decrease by 1.
     *
     * @throws java.util.NoSuchElementException if pool is empty.
     *
     * @return the tile taken from the pool
     */
    public Tile takeTile() {
        // TODO: implement code
        throw new UnsupportedOperationException();
    }

}
