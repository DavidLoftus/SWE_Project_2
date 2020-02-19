package scrabble;

import java.io.InputStream;
import java.util.Scanner;

public class Board {

    private Square[][] grid = new Square[15][15];

    public Board() {
        setupBoardContents();
    }

    private Square.Modifier charToEnum(char c) {
        switch (c) {
            case ' ':
                return Square.Modifier.NORMAL;
            case 'd':
                return Square.Modifier.DOUBLE_LETTER;
            case 'D':
                return Square.Modifier.DOUBLE_WORD;
            case 't':
                return Square.Modifier.TRIPLE_LETTER;
            case 'T':
                return Square.Modifier.TRIPLE_WORD;
            case '*':
                return Square.Modifier.STAR;
            default:
                throw new IllegalArgumentException("Bad character in board map: " + c);
        }
    }

    private void setupBoardContents(Scanner sc) {
        for (int i = 0; i < 15; ++i) {
            String row = sc.nextLine();
            assert row.length() == 15;
            for (int j = 0; j < 15; ++j) {
                grid[i][j] = new Square(charToEnum(row.charAt(j)));
            }
        }
    }

    private void setupBoardContents() {
        InputStream stream = this.getClass().getResourceAsStream("map.txt");
        assert stream != null;

        setupBoardContents(new Scanner(stream));
    }

    public void reset() {
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    protected void setTile(int i, int j, Tile tile) {
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    public char getLetterAt(int i, int j) {
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    boolean checkWordPlacement(WordPlacement wordPlacement) {
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    public void applyWordPlacement(Tile tile) {
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    public void printBoard() {
        System.out.println("+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+");
        for (int i = 0; i < 15; ++i) {
            System.out.print("|");
            for (int j = 0; j < 15; ++j) {
                System.out.print(grid[i][j].toString() + "|");
            }
            System.out.println("\n+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+");
        }
    }
}
