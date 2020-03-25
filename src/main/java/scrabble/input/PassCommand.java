package scrabble.input;

public class PassCommand implements InputCommand {

    @Override
    public boolean usesTurn() {
        return false;
    }

    public static InputCommand valueOf(String str) {
        if (str.equalsIgnoreCase("PASS")) {
            return new PassCommand();
        }
        return null;
    }
}
