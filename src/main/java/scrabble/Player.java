package scrabble;

public class Player {
	
	public String name;
	public int score = 0;
	public Frame frame = new Frame();

    public Player(String name) {
    	this.name = name;
    }

    public Player reset(String name) {
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

