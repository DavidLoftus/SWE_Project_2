package scrabble;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class PoolTest {
	
	
	
    @Test
    void reset() {
    	Pool pool = new Pool();
    	assertEquals(100, pool.size());
    }

    @Test
    void size() {
    	Pool pool = new Pool();
    	assertEquals(100, pool.size());
    	
    	pool.takeTile();
    	assertEquals(99, pool.size());
    	
    	for(int i = 98; i >=0; i--) {
    		pool.takeTile();
    		assertEquals(i, pool.size());
    	}
    	
    	assertEquals(0, pool.size());
    }
    
    @Test
    void testInitialContents() {
    	
    }

    @Test
    void isEmpty() {
    	Pool pool = new Pool();
    	assertFalse(pool.isEmpty());
    	
    	pool.takeTile();
    	assertFalse(pool.isEmpty());
    	
    	for(int i = 98; i > 0; i--) {
    		pool.takeTile();
    		assertFalse(pool.isEmpty());
    	}
    	
    	pool.takeTile();
    	assertTrue(pool.isEmpty());
    }

    @Test
    void takeTile() {
    }

}