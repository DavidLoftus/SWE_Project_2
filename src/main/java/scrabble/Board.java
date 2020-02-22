package scrabble;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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
        setupBoardContents();
    }

    protected void setTile(int i, int j, Tile tile) {
        if (tile == Tile.BLANK) {
            throw new IllegalArgumentException();
        }

        grid[i][j].setTile(tile);
    }

    public char getLetterAt(int i, int j) {
        if (grid[i][j].getLetter() == 0) {
            throw new IllegalArgumentException();
        }
        return grid[i][j].getLetter();
    }

    public boolean hasTileAt(int i, int j) {
        if (i < 0 || i >= 15 || j < 0 || j >= 15) {
            return false;
        }
        return !grid[i][j].isEmpty();
    }

    public Square.Modifier getModiferAt(int i, int j) {
        return grid[i][j].getModifier();
    }

    public void applyWordPlacement(Player player, WordPlacement wordPlacement) {
        List<Tile> neededTiles = getNeededTiles(wordPlacement);
        List<Tile> tilesToPlace = player.getFrame().getTilesToPlace(neededTiles);

        placesTiles(wordPlacement, tilesToPlace);
    }

    private void placesTiles(WordPlacement wordPlacement, List<Tile> tilesToPlace) {
        int j = 0;
        for (int i = 0; i < wordPlacement.length(); i++) {
            int column = wordPlacement.getColumnForLetter(i);
            int row = wordPlacement.getRowForLetter(i);
            if (grid[row][column].isEmpty()) {
                setTile(row, column, tilesToPlace.get(j));
                j++;
            }
        }
    }

    public List<Tile> getNeededTiles(WordPlacement wordPlace) {
        ArrayList<Tile> tileList = new ArrayList<>();

        for (int i = 0; i < wordPlace.length(); i++) {
            Tile tile = Tile.parseTile(wordPlace.getLetterAt(i));

            int row = wordPlace.getRowForLetter(i);
            int column = wordPlace.getColumnForLetter(i);

            if (grid[row][column].isEmpty()) {
                tileList.add(tile);
            } else if (grid[row][column].getLetter() != tile.getLetter()) {
                return null;
            }
        }

        return tileList;
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
