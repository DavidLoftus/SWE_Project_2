package scrabble;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PoolTest {

    private Pool pool;

    @BeforeEach
    void initPool() {
        pool = new Pool();
    }

    @Test
    void reset() {
        pool.takeTile();
        pool.reset();

        assertEquals(100, pool.size());

        testInitialContents();
    }

    @Test
    void size() {
        assertEquals(100, pool.size());

        pool.takeTile();
        assertEquals(99, pool.size());

        for (int i = 98; i >= 0; i--) {
            pool.takeTile();
            assertEquals(i, pool.size());
        }

        assertEquals(0, pool.size());
    }

    @Test
    void testInitialContents() {
        int[] counts = new int[Tile.values().length];

        while (!pool.isEmpty()) {
            Tile tile = pool.takeTile();
            counts[tile.ordinal()]++;
        }

        for (Tile tile : Tile.values()) {
            assertEquals(tile.getStartingCount(), counts[tile.ordinal()]);
        }
    }

    @Test
    void isEmpty() {
        assertFalse(pool.isEmpty());

        pool.takeTile();
        assertFalse(pool.isEmpty());

        for (int i = 98; i > 0; i--) {
            pool.takeTile();
            assertFalse(pool.isEmpty());
        }

        pool.takeTile();
        assertTrue(pool.isEmpty());
    }

    @Test
    void takeTile() {
        pool.takeTile();

        for (int i = 98; i >= 0; i--) {
            pool.takeTile();
        }

        assertTrue(pool.isEmpty());
        assertThrows(NoSuchElementException.class, pool::takeTile);
    }

    @Test
    void testToString() {
        Pool pool = new Pool();
        assertEquals("Pool[2*BLANK, 9*A, 2*B, 2*C, 4*D, 12*E, 2*F, 3*G, 2*H, 9*I, J, K, 4*L, 2*M, 6*N, 8*O, 2*P, Q, 6*R, 4*S, 6*T, 4*U, 2*V, 2*W, X, 2*Y, Z]", pool.toString());

        for (int i = 0; i < 100; i++) {
            pool.takeTile();
        }

        assertEquals("Pool[]", pool.toString());
    }
}
