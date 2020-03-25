package scrabble;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import scrabble.input.ExchangeCommand;
import scrabble.input.HelpCommand;
import scrabble.input.InputCommand;
import scrabble.input.PlaceCommand;

public class InputCommandTest {
    @Test
    void testInputCommand() {
        assertTrue(InputCommand.valueOf("EXCHANGE ABCD") instanceof ExchangeCommand);
        assertTrue(InputCommand.valueOf("HELP") instanceof HelpCommand);
        assertTrue(InputCommand.valueOf("A7 A ABCD") instanceof PlaceCommand);
    }

    @Test
    void testExchangeCommand() {
        List<Tile> tileList = new ArrayList<>();
        tileList.add(Tile.A);
        tileList.add(Tile.B);
        tileList.add(Tile.C);
        tileList.add(Tile.D);

        ExchangeCommand exchange = new ExchangeCommand(tileList);
        assertEquals(exchange.tiles, tileList);
    }

    @Test
    void testPlaceCommand() {
        WordPlacement wordPlacement =
                new WordPlacement(new BoardPos(7, 7), WordPlacement.Direction.HORIZONTAL, "hello");

        WordPlacement.Direction direction = WordPlacement.Direction.HORIZONTAL;
        PlaceCommand place = new PlaceCommand(wordPlacement);
        BoardPos pos = new BoardPos(7, 7);
        String word = "HELLO";

        assertEquals(place.wordPlacement.getDirection(), direction);
        assertEquals(place.wordPlacement.getLetterAt(0), word.charAt(0));
        assertEquals(place.wordPlacement.getLetterAt(1), word.charAt(1));
        assertEquals(place.wordPlacement.getLetterAt(2), word.charAt(2));
        assertEquals(place.wordPlacement.getLetterAt(3), word.charAt(3));
        assertEquals(place.wordPlacement.getLetterAt(4), word.charAt(4));
        assertEquals(place.wordPlacement.getStartPos(), pos);
    }
}
