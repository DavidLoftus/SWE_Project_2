package scrabble;

import java.io.InputStream;
import java.util.ArrayList;
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
        if (tile == Tile.BLANK) {
            throw new IllegalArgumentException();
        }

        grid[i][j].setTile(tile);
    }

    public char getLetterAt(int i, int j) {
        return grid[i][j].getLetter();
    }

    boolean checkWordPlacement(WordPlacement wordPlacement) {
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    public void applyWordPlacement(Tile tile) {
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    public Tile[] getNeededTiles(WordPlacement wordPlace) {
        ArrayList<Character> list = new ArrayList<Character>();
        ArrayList<Tile> tileList = new ArrayList<Tile>();
        Tile[] tileArray = null;

        for (int i = 0; i < wordPlace.length(); i++) {
            list.add(wordPlace.word.charAt(i));
            tileList.add(i, Tile.parseTile(wordPlace.word.charAt(i)));
        }
        /* debug */
        // System.out.println(list);
        System.out.println(tileList);

        tileArray = tileList.toArray(new Tile[0]);

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (grid[i][j].isEmpty()) {
                    return tileArray;
                } else {
                    if (grid[i][j].getLetter() != list.get(i)) {
                        return null;
                    }
                }
            }
        }

        return tileArray;
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
