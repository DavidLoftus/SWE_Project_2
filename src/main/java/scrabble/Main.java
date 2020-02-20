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
        int coordinate = 2;
        WordPlacement tempWord = new WordPlacement();
        System.out.println(tempWord.getRowForLetter(coordinate));
        tempWord.word = "Vehicle";
        System.out.println(tempWord.length());
        System.out.println(tempWord.getLetterAt(3));
        board.getNeededTiles(tempWord);

    }
}
