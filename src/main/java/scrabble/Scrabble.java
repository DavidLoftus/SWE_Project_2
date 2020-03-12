package scrabble;

import java.io.PrintStream;
import scrabble.exceptions.BadWordPlacementException;
import scrabble.input.ExchangeCommand;
import scrabble.input.HelpCommand;
import scrabble.input.InputCommand;
import scrabble.input.PlaceCommand;

public class Scrabble {

    private Pool pool = new Pool();
    private Board board = new Board();
    private Player[] players;
    private int currentPlayer;
    private PrintStream logOutput;

    public Scrabble(String[] names) {
        this.players = new Player[names.length];

        for (int i = 0; i < names.length; ++i) {
            players[i] = new Player(names[i]);
        }

        reset();
    }

    public void acceptCommand(InputCommand command) {
        Player player = players[currentPlayer];
        if (command instanceof PlaceCommand) {
            PlaceCommand place = (PlaceCommand) command;
            try {
                board.applyWordPlacement(player, place.wordPlacement);
            } catch (BadWordPlacementException e) {
                logOutput.println("Some error");
            }
            currentPlayer = (currentPlayer + 1) % players.length;
        } else if (command instanceof HelpCommand) {
            logOutput.println("To Exchange: EXCHANGE <letters>");
            logOutput.println("To Place: <grid ref> <across/down> <word>");
        } else if (command instanceof ExchangeCommand) {
            ExchangeCommand exchange = (ExchangeCommand) command;
            if (player.getFrame().hasTiles(exchange.tiles)) {
                for (int i = 0; i < exchange.toString().length(); i++) {
                    player.getFrame().removeTile(Tile.parseTile(exchange.toString().charAt(i)));
                }
                player.getFrame().refill(pool);
            } else {
                logOutput.println("Error");
            }
        } else logOutput.println("No such command");
    }

    public void reset() {
        board.reset();
        pool.reset();
        for (Player player : players) {
            player.reset();
            player.getFrame().refill(pool);
        }
        currentPlayer = 0;
    }
}
