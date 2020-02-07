package scrabble;

public class Main {

    public static void main(String[] args) {
        Pool pool = new Pool();
        Player player = new Player("Yams");

        System.out.println(player);
        System.out.println(pool);


        player.getFrame().refill(pool);
        System.out.println(player);
        System.out.println(pool);

        player.increaseScore(5);
        System.out.println(player);

        player.reset();
        System.out.println(player);
    }

}
