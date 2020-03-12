package scrabble.input;

import java.util.List;
import scrabble.Tile;

public class ExchangeCommand implements InputCommand {

    public List<Tile> tiles;

    @Override
    public boolean usesTurn() {
        return true;
    }

    static ExchangeCommand valueOf(String str) {
        return null;
    }
}
