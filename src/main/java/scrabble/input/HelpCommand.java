package scrabble.input;

public class HelpCommand implements InputCommand {
    @Override
    public boolean usesTurn() {
        return false;
    }
}
