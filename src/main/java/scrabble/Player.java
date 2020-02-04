package scrabble;

public class Player {

    private String name;
    private int score = 0;
    private Frame frame = new Frame();

    /**
     * sets the players name
     *
     * @param name
     */
    public Player(String name) {
        this.name = name;
    }
    /** Resets the players scores back to zero and gives them a new frame. */
    public void reset() {
        this.score = 0;
        this.frame = new Frame();
    }
    /**
     * Increases the score of a player by amount.
     *
     * @param amount to increase score by.
     */
    public void increaseScore(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount cant be negative");
        }
        this.score += amount;
    }

    /** @return the player's current score */
    public int getScore() {
        return this.score;
    }

    /** @return the frame of the player */
    public Frame getFrame() {
        return this.frame;
    }
    /**
     * Sets the player's name.
     *
     * @param name is to be the new name of the player
     */
    public void setName(String name) {
        this.name = name;
    }
    /** @return the name of the player */
    public String getName() {
        return this.name;
    }
}
