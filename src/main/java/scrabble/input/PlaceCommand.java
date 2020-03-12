package scrabble.input;

public class PlaceCommand implements InputCommand {

    @Override
    public boolean usesTurn() {
        return true;
    }

    static PlaceCommand valueOf(String str) {
        return null;
    }
}
