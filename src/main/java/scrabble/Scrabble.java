package scrabble;

public class Scrabble {

    private Pool pool = new Pool();
    private Board board = new Board();
    private Player[] players;
    private int currentPlayer;

    public Scrabble(String[] names) {
        this.players = new Player[names.length];

        for (int i = 0; i < names.length; ++i) {
            players[i] = new Player(names[i]);
        }

        reset();
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
