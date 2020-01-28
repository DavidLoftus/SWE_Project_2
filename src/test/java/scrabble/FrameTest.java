package scrabble;

import static org.junit.jupiter.api.Assertions.*;

class FrameTest {
    /**
     * FakePool acts as a Pool containing specified tiles in a deterministic order.
     *
     * Tiles are added to the pool in a FIFO queue-like fashion.
     * This helps with testing when we want to know exact contents of Pool
     */
    protected static class FakePool extends Pool {
        private Queue<Tile> tilesToReturn = new ArrayDeque<>();

        @Override
        public boolean isEmpty() {
            return tilesToReturn.isEmpty();
        }

        @Override
        public int size() {
            return tilesToReturn.size();
        }

        void add(Tile tile) {
            tilesToReturn.add(tile);
        }

        @Override
        public Tile takeTile() {
            return tilesToReturn.remove();
        }
    }

}