package scrabble.input;

public class ExchangeCommand implements InputCommand {

    @Override
    public boolean usesTurn() {
        return true;
    }

    static ExchangeCommand valueOf(String str) {
        return null;
    }
}
