package scrabble.exceptions;

import scrabble.WordPlacement;

public class BadWordPlacementException extends Exception {
    private WordPlacement wordPlacement;

    public BadWordPlacementException(WordPlacement wordPlacement, String message) {
        super(message);
        this.wordPlacement = wordPlacement;
    }

    public BadWordPlacementException(WordPlacement wordPlacement, String message, Throwable cause) {
        super(message, cause);
        this.wordPlacement = wordPlacement;
    }
}
