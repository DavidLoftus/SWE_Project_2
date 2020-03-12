package scrabble.input;

public interface InputCommand {

    boolean usesTurn();

    static InputCommand valueOf(String str) {
        if (str.startsWith("EXCHANGE")) {
            return ExchangeCommand.valueOf(str);
        } else {
            return PlaceCommand.valueOf(str);
        }
    }
}
