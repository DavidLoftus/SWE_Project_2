package scrabble.input;

import scrabble.WordPlacement;

public class PlaceCommand implements InputCommand {

    public WordPlacement wordPlacement;

    @Override
    public boolean usesTurn() {
        return true;
    }

    static PlaceCommand valueOf(String str) {
        return null;
    }
}
