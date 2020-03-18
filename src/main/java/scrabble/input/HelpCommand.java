package scrabble.input;

public class HelpCommand implements InputCommand {
    @Override
    public boolean usesTurn() {
        return false;
    }

    public static InputCommand valueOf(String str) {
        if (str.equalsIgnoreCase("HELP")) {
            return new HelpCommand();
        }
        return null;
    }
}
