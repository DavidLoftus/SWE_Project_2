package scrabble;

public class Main {

    public static void main(String[] args) {
        Board board = new Board();
        Pool pool = new Pool();
        Player player = new Player("Player");

        System.out.println(player);
        System.out.println(pool);

        player.getFrame().refill(pool);
        System.out.println(player);
        System.out.println(pool);

        player.increaseScore(5);
        System.out.println(player);

        player.reset();
        System.out.println(player);

        System.out.println("");
        board.printBoard();
        System.out.println("\n");
        board.setTile(7, 7, Tile.X);
        board.printBoard();
        System.out.println(board.getLetterAt(7, 7));

        System.out.println("");
        WordPlacement tempWord = new WordPlacement();
        tempWord.word = "Vehicle";
        System.out.println(tempWord.getRowForLetter(3));
        System.out.println(tempWord.getColumnForLetter(4));
        System.out.println(tempWord.length());
        System.out.println(tempWord.getLetterAt(3));
        System.out.println(board.getNeededTiles(tempWord));
    }
}
