package scrabble;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PlayerTest {

    @Test
    void testName() {
        Player player = new Player("John");

        assertEquals("John", player.getName());

        player.setName("Doe");

        assertEquals("Doe", player.getName());
    }

    @Test
    void testScore() {
        Player player = new Player("James");

        assertEquals(0, player.getScore());

        player.increaseScore(10);
        assertEquals(10, player.getScore());
    }

    @Test
    void testReset() {
        Player player = new Player("Steven");

        player.increaseScore(25);
        assertEquals(25, player.getScore());

        player.reset();
        assertEquals(0, player.getScore());
    }

    @Test
    void testToString() {
        Player player = new Player("David");
        assertEquals("David (0) []", player.toString());
        player.increaseScore(30);
        assertEquals("David (30) []", player.toString());
        FrameTest.FakePool pool = new FrameTest.FakePool();

        pool.add(Tile.BLANK);
        pool.add(Tile.A);
        pool.add(Tile.B);
        pool.add(Tile.C);
        pool.add(Tile.G);
        pool.add(Tile.E);
        pool.add(Tile.BLANK);

        player.getFrame().refill(pool);

        assertEquals("David (30) [BLANK, A, B, C, G, E, BLANK]", player.toString());
    }
}
