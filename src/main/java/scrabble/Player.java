package scrabble;

public class Player {
	
	 // TODO: implement fields
	public String name;
	public int score;
	public Frame frame;

    public Player(String name) {
        // TODO: implement code
    }

    public Player reset(String name) {
        // TODO: implement code 
    	this.name = name;
    	this.score = 0;
        throw new UnsupportedOperationException();
    }

    public void increaseScore(int amount) {
        // TODO: implement code
    	if(amount < 0) {
    		throw new UnsupportedOperationException();
    	}
    	this.score += amount;
    }
    
    public int getScore() {
        // TODO: implement code
        // new UnsupportedOperationException();
    	return this.score;
    }

    public Frame getFrame() {
        // TODO: implement code
        //throw new UnsupportedOperationException();
    	return this.frame;
    }

    public void setName(String name) {
        // TODO: implement code
    	this.name = name;
        throw new UnsupportedOperationException();
    }
    public String getName() {
        // TODO: implement code
        //throw new UnsupportedOperationException();
        return this.name;
    }
}

