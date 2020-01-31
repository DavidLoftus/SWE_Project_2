package scrabble;

public class Player {
	
	private String name;
	private int score = 0;
	private Frame frame = new Frame();

    public Player(String name) {
    	this.name = name;
    }

    public void reset() {
    	this.score = 0;
    	this.frame = new Frame();
    }

    public void increaseScore(int amount) {
    	if(amount < 0) {
    		throw new IllegalArgumentException("amount cant be negative");
    	}
    	this.score += amount;
    }
    
    public int getScore() {
    	return this.score;
    }

    public Frame getFrame() {
    	return this.frame;
    }

    public void setName(String name) {
    	this.name = name;
    }
    public String getName() {
        return this.name;
    }
}

