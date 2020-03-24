package scrabble;

import java.io.PrintStream;
import scrabble.exceptions.BadWordPlacementException;
import scrabble.gui.ScrabbleController;
import scrabble.input.*;

public class Scrabble implements InputListener {

    ScrabbleController uiController;
    PrintStream logOutput;

    private Pool pool = new Pool();
    private Board board = new Board();

    private Player[] players = null;

    private int currentPlayer = 0;

    public Scrabble(ScrabbleController uiController) {
        this.uiController = uiController;
        this.logOutput = uiController.commandPanel.getOutputStream();

        uiController.boardGrid.setBoard(board);

        requestPlayerNames();
    }

    private void requestPlayerNames() {
        InputEventHandler inputHandler = uiController.commandPanel.getInputEventHandler();

        logOutput.println("Player 1 what is your name?");
        inputHandler.addOneTimeListener(
                player1Name -> {
                    logOutput.printf("Player 1 set to %s\n", player1Name);

                    logOutput.println("Player 2 what is your name?");

                    inputHandler.addOneTimeListener(
                            player2Name -> {
                                logOutput.printf("Player 2 set to %s\n", player2Name);

                                players =
                                        new Player[] {
                                            new Player(player1Name), new Player(player2Name)
                                        };

                                logOutput.println("Starting game...");

                                startGame();
                            });
                });
    }

    private Player nextPlayer(int i) {
        currentPlayer = i;
        Player player = players[currentPlayer];

        logOutput.printf("%s it is your turn please make a move: \n", player.getName());

        uiController.frame.setFrame(player.getFrame());

        return player;
    }

    private Player nextPlayer() {
        return nextPlayer((currentPlayer + 1) % players.length);
    }

    private void startGame() {
        InputEventHandler inputHandler = uiController.commandPanel.getInputEventHandler();
        inputHandler.addListener(this);

        for (Player player : players) {
            player.getFrame().refill(pool);
        }

        Player player = nextPlayer(0);
    }

    public void accept(String inputStr) {
        InputCommand command = InputCommand.valueOf(inputStr);
        if (command == null) {
            logOutput.println("Bad command.");
            return;
        }

        Player player = players[currentPlayer];
        if (command instanceof PlaceCommand) {
            PlaceCommand place = (PlaceCommand) command;
            try {
                int score = board.applyWordPlacement(player, place.wordPlacement);
                uiController.boardGrid.updateGridTiles();
                player.increaseScore(score);
                logOutput.printf(
                        "Success! Added %d to your score, total: %d\n", score, player.getScore());
                nextPlayer();
            } catch (BadWordPlacementException e) {
                logOutput.printf("Failed to place word: %s\n", e.getMessage());
            }
            currentPlayer = (currentPlayer + 1) % players.length;
        } else if (command instanceof HelpCommand) {
            logOutput.println("To Exchange: EXCHANGE <letters>");
            logOutput.println("To Place: <grid ref> <across/down> <word>");
        } else if (command instanceof ExchangeCommand) {
            ExchangeCommand exchange = (ExchangeCommand) command;
            if (player.getFrame().hasTiles(exchange.tiles)) {
                player.getFrame().removeTiles(exchange.tiles);
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

    public Pool getPool() {
        return pool;
    }

    public Board getBoard() {
        return board;
    }

    public Player[] getPlayers() {
        return players;
    }
}
