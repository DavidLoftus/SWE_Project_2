package scrabble;

public class Player {
	
	private String name;
	private int score = 0;
	private Frame frame = new Frame();

	/**
	 * sets the players name
	 * @param name
	 */
    public Player(String name) {
    	this.name = name;
    }
    /**
     * sets the players scores back to zero and 
     * gives them a new frame. 
     */
    public void reset() {
    	this.score = 0;
    	this.frame = new Frame();
    }
    /**
     * this increases the score of a player by amount.
     * @param amount
     */
    public void increaseScore(int amount) {
    	if(amount < 0) {
    		throw new IllegalArgumentException("amount cant be negative");
    	}
    	this.score += amount;
    }
    
    /**
     * @return the current score
     */
    public int getScore() {
    	return this.score;
    }
    
    /**
     * @return the frame of a player
     */
    public Frame getFrame() {
    	return this.frame;
    }
    /**
     * @param name
     */
    public void setName(String name) {
    	this.name = name;
    }
    /**
     * @return the name of the player
     */
    public String getName() {
        return this.name;
    }
}

