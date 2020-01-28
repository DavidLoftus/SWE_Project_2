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

    @Test
    void removeTile() {
        Frame frame = new Frame();

        FakePool pool = new FakePool();
        pool.add(Tile.BLANK);
        pool.add(Tile.A);
        pool.add(Tile.B);
        pool.add(Tile.C);
        pool.add(Tile.G);
        pool.add(Tile.E);
        pool.add(Tile.BLANK);

        frame.refill(pool);

        assertTrue(frame.hasTile(Tile.BLANK));

        frame.removeTile(Tile.BLANK);
        assertTrue(frame.hasTile(Tile.BLANK));

        frame.removeTile(Tile.BLANK);
        assertFalse(frame.hasTile(Tile.BLANK));

        assertTrue(frame.hasTile(Tile.A));

        frame.removeTile(Tile.A);
        assertFalse(frame.hasTile(Tile.A));
    }

    @Test
    void hasTile() {
        Frame frame = new Frame();

        FakePool pool = new FakePool();
        pool.add(Tile.BLANK);
        pool.add(Tile.A);
        pool.add(Tile.B);
        pool.add(Tile.C);
        pool.add(Tile.G);
        pool.add(Tile.E);
        pool.add(Tile.BLANK);

        frame.refill(pool);

        assertTrue(frame.hasTile(Tile.BLANK));
        assertTrue(frame.hasTile(Tile.A));
        assertTrue(frame.hasTile(Tile.B));
        assertTrue(frame.hasTile(Tile.C));
        assertTrue(frame.hasTile(Tile.E));
        assertTrue(frame.hasTile(Tile.G));

        assertFalse(frame.hasTile(Tile.F));
        assertFalse(frame.hasTile(Tile.Z));
    }

    @Test
    void isEmpty() {
        Frame frame = new Frame();
        assertTrue(frame.isEmpty());

        Pool pool = new Pool();
        frame.refill(pool);

        assertFalse(frame.isEmpty());
    }
}