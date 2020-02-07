package scrabble;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PlayerTest {

    @Test
    void testName() {
        Player newPlayer = new Player("John");

        assertEquals("John", newPlayer.getName());

        newPlayer.setName("Doe");

        assertEquals("Doe", newPlayer.getName());
    }

    @Test
    void testScore() {
        Player newPlayer = new Player("James");

        assertEquals(0, newPlayer.getScore());

        newPlayer.increaseScore(10);
        assertEquals(10, newPlayer.getScore());
    }

    @Test
    void testReset() {
        Player newPlayer = new Player("Steven");

        newPlayer.increaseScore(25);
        assertEquals(25, newPlayer.getScore());

        newPlayer.reset();
        assertEquals(0, newPlayer.getScore());
    }
    
    @Test
    void testToString() {
    	Player newPlayer = new Player("David");
    	assertEquals("David (0) [       ]", newPlayer.toString());
    	newPlayer.increaseScore(30);
    	assertEquals("David (30) [       ]", newPlayer.toString());
    	FrameTest.FakePool pool = new FrameTest.FakePool();
    	
    	pool.add(Tile.BLANK);
        pool.add(Tile.A);
        pool.add(Tile.B);
        pool.add(Tile.C);
        pool.add(Tile.G);
        pool.add(Tile.E);
        pool.add(Tile.BLANK);
        
        assertEquals("David (30) [BLANK A B C G E BLANK]", newPlayer.toString());
    }
}
