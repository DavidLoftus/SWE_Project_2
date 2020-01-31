package scrabble;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void testName(){
        Player newPlayer = new Player("John");

        assertEquals("John", newPlayer.getName());

        newPlayer.setName("Doe");

        assertEquals("Doe", newPlayer.getName());
    }

    @Test
    void testScore(){
        Player newPlayer = new Player("James");

        assertEquals(0, newPlayer.getScore());

        newPlayer.increaseScore(10);
        assertEquals(10, newPlayer.getScore());
    }

    @Test
    void testReset(){
        Player newPlayer = new Player("Steven");

        newPlayer.increaseScore(25);
        assertEquals(25, newPlayer.getScore());

        newPlayer.reset();
        assertEquals(0, newPlayer.getScore());
    }
}