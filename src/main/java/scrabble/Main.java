package scrabble;

import scrabble.exceptions.BadWordPlacementException;

public class Main {

    public static void main(String[] args) throws BadWordPlacementException {
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
        WordPlacement tempWord = new WordPlacement(4, 2, WordPlacement.Direction.VERTICAL, "apple");
        System.out.println("getRow: " + tempWord.getRowForLetter(3));
        System.out.println("getCol: " + tempWord.getColumnForLetter(4));
        System.out.println("Word Length: " + tempWord.length());
        System.out.println("LetterAt: " + tempWord.getLetterAt(2));
        System.out.println(board.getNeededTiles(tempWord));
    }
}
