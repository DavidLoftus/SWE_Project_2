package scrabble;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void reset() {
        Player player = new Player("Bob");

        player.increaseScore(17);
        assertEquals(17, player.getScore());

        player.reset();
        assertEquals(0, player.getScore());
    }

    @Test
    void testScore() {
        Player player = new Player("Bob");

        // Player should start with score 0
        assertEquals(0, player.getScore());

        player.increaseScore(10);
        assertEquals(10, player.getScore());

        player.increaseScore(7);
        assertEquals(17, player.getScore());

        player.increaseScore(0);
        assertEquals(17, player.getScore());

        assertThrows(IllegalArgumentException.class, () -> player.increaseScore(-1));
    }

    @Test
    void getFrame() {
        Player player = new Player("Bob");

        assertNotNull(player.getFrame());
    }

    @Test
    void setName() {
        Player player = new Player("Bob");

        player.setName("Alice");
        assertEquals("Alice", player.getName());
    }

    @Test
    void getName() {
        Player player = new Player("Bob");

        assertEquals("Bob", player.getName());
    }
}