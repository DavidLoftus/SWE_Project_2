package scrabble;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import scrabble.exceptions.BadWordPlacementException;

public class Main {

    public static void main(String[] args) {
        Board board = new Board();
        Pool pool = new Pool();
        Player player = new Player("Player");

        player.getFrame().refill(pool);

        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                board.printBoard();
                System.out.println(player);

                System.out.println("Please enter row:");
                int i = sc.nextInt();

                System.out.println("Please enter column:");
                int j = sc.nextInt();

                BoardPos pos = new BoardPos(i, j);

                System.out.println("HORIZONTAL or VERTICAL:");
                WordPlacement.Direction direction =
                        WordPlacement.Direction.valueOf(sc.next().toUpperCase());

                System.out.println("Enter your word:");
                String word = sc.next();

                WordPlacement wordPlacement = new WordPlacement(pos, direction, word);

                try {
                    board.applyWordPlacement(player, wordPlacement);
                    System.out.println("Word placement successful.");

                    player.getFrame().refill(pool);
                } catch (BadWordPlacementException e) {
                    System.out.printf("Failed to place word: %s\n", e.getMessage());
                }

                TimeUnit.SECONDS.sleep(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
