package scrabble;

import java.io.PrintStream;
import scrabble.exceptions.BadWordPlacementException;
import scrabble.gui.ScrabbleController;
import scrabble.input.*;
import scrabble.wordlist.WordList;

public class Scrabble implements InputListener {

    ScrabbleController uiController;
    PrintStream logOutput;

    private Pool pool = new Pool();
    private Board board = new Board();

    private Player[] players = null;

    private int currentPlayer = 0;

    private Board.AppliedWordPlacement lastAppliedWordPlacement = null;

    private WordList wordList = new WordList();

    /**
     * Initializes the class to be able to controlled through the GUI.
     *
     * @param uiController the GUI controller
     */
    public Scrabble(ScrabbleController uiController) {
        this.uiController = uiController;
        this.logOutput = uiController.commandPanel.getOutputStream();

        uiController.boardGrid.setBoard(board);

        requestPlayerNames();
    }

    /** Asks the user to input and set players' names */
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

                                uiController.player1.setPlayer(players[0]);
                                uiController.player2.setPlayer(players[1]);

                                logOutput.println("Starting game...");

                                startGame();
                            });
                });
    }

    /**
     * This sets the frame based on which player's turn it is.
     *
     * @param i iteration of turns between the players.
     * @return the player who's turn it is.
     */
    private Player nextPlayer(int i) {
        currentPlayer = i;
        Player player = players[currentPlayer];

        player.getFrame().refill(pool);

        logOutput.printf("%s it is your turn please make a move: \n", player.getName());

        uiController.player1.update();
        uiController.player2.update();

        return player;
    }

    /** @return the next player */
    private Player nextPlayer() {
        return nextPlayer((currentPlayer + 1) % players.length);
    }

    /** Initializes the start of the game */
    private void startGame() {
        InputEventHandler inputHandler = uiController.commandPanel.getInputEventHandler();
        inputHandler.addListener(this);

        for (Player player : players) {
            player.getFrame().refill(pool);
        }

        uiController.player1.update();
        uiController.player2.update();

        nextPlayer(0);
    }

    /**
     * Asks the user for input commands which outputs a print based on what the user inputted. There
     * are three types of acceptable commands of which are HELP, PLACE and EXCHANGE. If the user
     * inputs a command that is not any of those three he/she will get a 'bad command' print.
     *
     * @param inputStr user input
     */
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
                lastAppliedWordPlacement = board.applyWordPlacement(player, place.wordPlacement);
                player.increaseScore(lastAppliedWordPlacement.score);

                logOutput.printf(
                        "Success! Added %d to your score, total: %d\n",
                        lastAppliedWordPlacement.score, player.getScore());

                // Refresh UI elements
                uiController.boardGrid.updateGridTiles();

                nextPlayer();
            } catch (BadWordPlacementException e) {
                logOutput.printf("Failed to place word: %s\n", e.getMessage());
            }
        } else if (command instanceof ExchangeCommand) {
            ExchangeCommand exchange = (ExchangeCommand) command;
            if (player.getFrame().hasTiles(exchange.tiles)) {

                player.getFrame().removeTiles(exchange.tiles);
                player.getFrame().refill(pool);

                lastAppliedWordPlacement = null;

                nextPlayer();
            } else {
                logOutput.println("Player doesn't have those tiles.");
            }
        } else if (command instanceof BasicCommand) {
            BasicCommand basicCommand = (BasicCommand) command;
            if (basicCommand.command.equals("PASS")) {
                logOutput.printf("%s has skipped their turn.\n", player.getName());
                lastAppliedWordPlacement = null;
                nextPlayer();
            } else if (basicCommand.command.equals("HELP")) {
                logOutput.println("To Exchange: EXCHANGE <letters>");
                logOutput.println("To Place: <grid ref> <across/down> <word>");
                logOutput.println("To skip turn: PASS");
                logOutput.println("To quit game: QUIT");
            } else if (basicCommand.command.equals("QUIT")) {
                logOutput.println("Thanks for playing!");
                System.exit(0);
            } else if (basicCommand.command.equals("CHALLENGE")) {
                if (lastAppliedWordPlacement != null) {
                    if (challengeWordPlacement()) {
                        logOutput.println("NOT VALID!");
//                        undoWordPlacement();
                    } else {
                        logOutput.println("VALID!");
                        nextPlayer();
                    }
                } else {
                    logOutput.println("You can't challenge right now.");
                }
                lastAppliedWordPlacement = null;
            }
        } else {
            logOutput.println("No such command");
        }
    }

    private void undoWordPlacement() {
        // TODO: implement
        this.lastAppliedWordPlacement = null;
        throw new UnsupportedOperationException();
    }

    private boolean challengeWordPlacement() {
        for (WordRange wordRange : lastAppliedWordPlacement.ranges) {
            StringBuilder sb = new StringBuilder();
            for (BoardPos pos : wordRange) {
                sb.append(board.getLetterAt(pos));
            }
            String word = sb.toString();

            if (!wordList.isValidWord(word)) {
                logOutput.printf("Word \"%s\" is not valid.\n", word);
                return true;
            }
        }
        return false;
    }

    /** Resets the current game being played. */
    public void reset() {
        board.reset();
        pool.reset();
        for (Player player : players) {
            player.reset();
            player.getFrame().refill(pool);
        }
        currentPlayer = 0;
    }

    /**
     * Accessor method to get pool.
     *
     * @return the pool of words
     */
    public Pool getPool() {
        return pool;
    }

    /**
     * Accessor method to get board.
     *
     * @return the board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Accessor method to get players.
     *
     * @return an array of players.
     */
    public Player[] getPlayers() {
        return players;
    }
}
