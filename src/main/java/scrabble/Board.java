package scrabble;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import scrabble.exceptions.BadWordPlacementException;

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

    private Square getSquareAt(BoardPos pos) {
        return grid[pos.getRow()][pos.getColumn()];
    }

    protected void setTile(BoardPos pos, Tile tile) {
        if (tile == Tile.BLANK) {
            throw new IllegalArgumentException();
        }

        getSquareAt(pos).setTile(tile);
    }

    public char getLetterAt(BoardPos pos) {
        Square square = getSquareAt(pos);
        if (square.getLetter() == 0) {
            throw new IllegalArgumentException();
        }
        return square.getLetter();
    }

    public boolean hasTileAt(BoardPos pos) {
        return !getSquareAt(pos).isEmpty();
    }

    public Square.Modifier getModiferAt(BoardPos pos) {
        return getSquareAt(pos).getModifier();
    }

    public void applyWordPlacement(Player player, WordPlacement wordPlacement)
            throws BadWordPlacementException {
        List<Tile> neededTiles = getNeededTiles(wordPlacement);
        List<Tile> tilesToPlace = player.getFrame().getTilesToPlace(neededTiles);
        if (tilesToPlace == null) {
            throw new BadWordPlacementException(
                    wordPlacement, "Player doesn't have enough tiles to place this");
        }
        if (tilesToPlace.isEmpty()) {
            throw new BadWordPlacementException(wordPlacement, "Must place atleast one new tile");
        }

        placesTiles(wordPlacement, tilesToPlace);
    }

    private void placesTiles(WordPlacement wordPlacement, List<Tile> tilesToPlace) {
        int j = 0;
        for (int i = 0; i < wordPlacement.length(); i++) {
            BoardPos pos = wordPlacement.getPositionAt(i);
            if (!hasTileAt(pos)) {
                setTile(pos, tilesToPlace.get(j));
                j++;
            }
        }
    }

    public List<Tile> getNeededTiles(WordPlacement wordPlacement) throws BadWordPlacementException {
        ArrayList<Tile> tileList = new ArrayList<>();

        for (int i = 0; i < wordPlacement.length(); i++) {
            Tile tile = Tile.parseTile(wordPlacement.getLetterAt(i));

            BoardPos pos = wordPlacement.getPositionAt(i);

            if (!hasTileAt(pos)) {
                tileList.add(tile);
            } else if (getLetterAt(pos) != tile.getLetter()) {
                throw new BadWordPlacementException(
                        wordPlacement, "Different letter already exists in placed location");
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
