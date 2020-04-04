package scrabble;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import scrabble.exceptions.BadWordPlacementException;
import scrabble.gui.SquareView;

/**
 * Board holds the the 15x15 grid of {@link Square} objects.
 *
 * <p>The public interface does not allow explicit placing of tiles. You may only place tiles using
 * the {@link #applyWordPlacement} method.
 *
 * <p>Board has no custom toString method. It can be printed to console using the {@link
 * #printBoard()} method.
 */
public class Board {

    private Square[][] grid = new Square[15][15];

    /** Initializes board, setting up empty grid with correct modifiers. */
    public Board() {
        setupBoardContents();
    }

    /**
     * Converts a character from map.txt resource to a {@link Square.Modifier} enum.
     *
     * <p>Legend is:
     *
     * <ul>
     *   <li>' ' => NORMAL
     *   <li>'d' => DOUBLE_LETTER
     *   <li>'D' => DOUBLE_WORD
     *   <li>'t' => TRIPLE_LETTER
     *   <li>'*' => STAR
     * </ul>
     *
     * @param c the character to transform
     * @return The modifier associated with that character
     * @throws IllegalArgumentException if c is not one of the above characters
     */
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

    /**
     * Initializes grid with given data in scanner
     *
     * <p>Scanner should contain 15 lines, each with 15 characters (not including newline) These
     * characters must be from the legend specified in {@link #charToEnum(char)}
     *
     * @param sc The scanner containing the map data
     */
    private void setupBoardContents(Scanner sc) {
        for (int i = 0; i < 15; ++i) {
            String row = sc.nextLine();
            assert row.length() == 15;
            for (int j = 0; j < 15; ++j) {
                grid[i][j] = new Square(charToEnum(row.charAt(j)));
            }
        }
    }

    /**
     * Initializes grid with contents from map.txt resource.
     *
     * <p>Calls {@link #setupBoardContents(Scanner)}
     */
    private void setupBoardContents() {
        InputStream stream = this.getClass().getResourceAsStream("map.txt");
        assert stream != null;

        setupBoardContents(new Scanner(stream));
    }

    /** Resets board to original state */
    public void reset() {
        setupBoardContents();
    }

    /**
     * Gets the {@link Square} object at the given location. This is private since access to Square
     * allows one to manually set the tiles.
     *
     * @param pos position of requested square
     * @return mutable {@link Square} object
     */
    private Square getSquareAt(BoardPos pos) {
        return grid[pos.getRow()][pos.getColumn()];
    }

    /**
     * Puts a tile in the given location. This method only works for non-BLANK tiles, since we don't
     * provide the letter.
     *
     * @param pos The position of destination square
     * @param tile The tile to place in square
     * @throws IllegalArgumentException if tile is {@link Tile#BLANK}
     */
    protected void setTile(BoardPos pos, Tile tile) {
        if (tile == Tile.BLANK) {
            throw new IllegalArgumentException("can't call setTile with BLANK tile");
        }

        getSquareAt(pos).setTile(tile);
    }

    /**
     * Returns the letter at the given position on the board. Should not be called if {@link
     * #hasTileAt(BoardPos)} returns false
     *
     * @param pos The position to request the letter at.
     * @return The letter at that position on the board
     * @throws IllegalArgumentException if no letter was present.
     */
    public char getLetterAt(BoardPos pos) {
        if (!hasTileAt(pos)) {
            throw new IllegalArgumentException();
        }
        return getSquareAt(pos).getLetter();
    }

    /**
     * Checks if a tile has been placed at given position on the board.
     *
     * @param pos The position in board to check
     * @return true if square at given position is not empty, otherwise false
     */
    public boolean hasTileAt(BoardPos pos) {
        return !getSquareAt(pos).isEmpty();
    }

    /**
     * Gets the modifier for square at given position on the board.
     *
     * @param pos The position in board to get the modifier for
     * @return the modifier at the given position
     */
    public Square.Modifier getModiferAt(BoardPos pos) {
        return getSquareAt(pos).getModifier();
    }

    public SquareView getSquareViewAt(BoardPos pos) {
        return new SquareView(getSquareAt(pos));
    }

    /**
     * Tries to apply a given WordPlacement move using player's tiles.
     *
     * @param player The player to take tiles from
     * @param wordPlacement specifies the letters and positions to place the tile at. BLANKs are
     *     automatically placed if necessary
     * @throws BadWordPlacementException if wordPlacement would cause an invalid move
     * @return the points to be awarded for the successful word placement.
     */
    public AppliedWordPlacement applyWordPlacement(Player player, WordPlacement wordPlacement)
            throws BadWordPlacementException {
        if (!wordPlacement.isConnectedToExistingTile(this) && !wordPlacement.isPlacedAtStar(this)) {
            throw new BadWordPlacementException(
                    wordPlacement, "Word must be placed on star or be neighbouring existing tile");
        }
        List<Tile> neededTiles = getNeededTiles(wordPlacement);
        List<Tile> tilesToPlace = player.getFrame().getTilesToPlace(neededTiles);
        if (tilesToPlace == null) {
            throw new BadWordPlacementException(
                    wordPlacement, "Player doesn't have enough tiles to place this");
        }

        List<BoardPos> placedPositions = placeTiles(wordPlacement, tilesToPlace);

        return new AppliedWordPlacement(player, placedPositions, wordPlacement);
    }

    public class AppliedWordPlacement {
        Player player;
        List<BoardPos> placedPositions;
        List<WordRange> ranges;
        int score;

        public AppliedWordPlacement(Player player, List<BoardPos> placedPositions, WordPlacement wordPlacement) {
            this.player = player;
            this.placedPositions = placedPositions;
            this.ranges = getWordRangesFromPlacement(placedPositions, wordPlacement.getDirection());
            this.score = 0;

            for (WordRange range : ranges) {
                this.score += getWordScore(placedPositions, range);
            }
        }
    }

    /**
     * Returns a list of word ranges based on its placed positions and direction.
     *
     * @param placedPositions the list of positions in the board
     * @param direction the direction of which the tile were placed
     * @return a list of all the WordRange from placement
     */
    private List<WordRange> getWordRangesFromPlacement(
            List<BoardPos> placedPositions, WordPlacement.Direction direction) {
        List<WordRange> rangeList = new ArrayList<>();

        if (direction == WordPlacement.Direction.HORIZONTAL) {
            rangeList.add(spanWordRangeHorizontal(placedPositions.get(0)));
        } else {
            rangeList.add(spanWordRangeVertical(placedPositions.get(0)));
        }

        for (BoardPos pos : placedPositions) {
            WordRange range;
            if (direction == WordPlacement.Direction.HORIZONTAL) {
                range = spanWordRangeVertical(pos);
            } else {
                range = spanWordRangeHorizontal(pos);
            }

            if (range.getLength() > 1) {
                rangeList.add(range);
            }
        }

        return rangeList;
    }

    /**
     * Returns a new WordRange object specifically if it spans horizontally.
     *
     * @param pos the position in board
     * @return a new WordRange object
     */
    private WordRange spanWordRangeHorizontal(BoardPos pos) {
        int row = pos.getRow();
        int col = pos.getColumn();
        int count = 1;
        BoardPos startPos = pos;

        for (int i = col - 1; i >= 0; i--) {
            BoardPos neighbour = new BoardPos(row, i);
            if (hasTileAt(neighbour)) {
                count++;
                startPos = neighbour;
            } else {
                break;
            }
        }

        for (int i = col + 1; i < 15; i++) {
            BoardPos neighbour = new BoardPos(row, i);
            if (hasTileAt(neighbour)) {
                count++;
            } else {
                break;
            }
        }

        return new WordRange(startPos, WordPlacement.Direction.HORIZONTAL, count);
    }

    /**
     * Returns a new WordRange object specifically if it spans horizontally.
     *
     * @param pos the position in board
     * @return a new WordRange object
     */
    private WordRange spanWordRangeVertical(BoardPos pos) {
        int row = pos.getRow();
        int col = pos.getColumn();
        int count = 1;
        BoardPos startPos = pos;

        for (int i = row - 1; i >= 0; i--) {
            BoardPos neighbour = new BoardPos(i, col);
            if (hasTileAt(neighbour)) {
                count++;
                startPos = neighbour;
            } else {
                break;
            }
        }

        for (int i = row + 1; i < 15; i++) {
            BoardPos neighbour = new BoardPos(i, col);
            if (hasTileAt(neighbour)) {
                count++;
            } else {
                break;
            }
        }

        return new WordRange(startPos, WordPlacement.Direction.VERTICAL, count);
    }

    /**
     * Returns the total score the word multiplied by possible achievable bonuses from Square.
     *
     * @param placedPositions the list of positions in the board
     * @param range the range of the word
     * @return the total score of the word
     */
    private int getWordScore(List<BoardPos> placedPositions, WordRange range) {
        int wordScore = 0;
        int multiplier = 1;
        for (BoardPos pos : range) {
            wordScore += getSquareAt(pos).getTile().getValue();
            if (placedPositions.contains(pos)) {
                switch (getModiferAt(pos)) {
                    case DOUBLE_WORD:
                        multiplier *= 2;
                        break;
                    case DOUBLE_LETTER:
                        wordScore += getSquareAt(pos).getTile().getValue();
                        break;
                    case TRIPLE_WORD:
                        multiplier *= 3;
                        break;
                    case TRIPLE_LETTER:
                        wordScore += 2 * getSquareAt(pos).getTile().getValue();
                        break;
                    default:
                }
            }
        }
        return wordScore * multiplier;
    }

    /**
     * Places tiles from tilesToPlace onto the board at positions specified by wordPlacement. This
     * method assumes input has already been checked in {@link #applyWordPlacement(Player,
     * WordPlacement)}
     *
     * @param wordPlacement specifies where to place the tile, and (in the case of a blank) what
     *     letter to place.
     * @param tilesToPlace The tiles belonging to the player to place on the board.
     * @return a list of positions in the board each tiles were placed in.
     */
    private List<BoardPos> placeTiles(WordPlacement wordPlacement, List<Tile> tilesToPlace) {
        int j = 0;
        List<BoardPos> positions = new ArrayList<>();

        for (int i = 0; i < wordPlacement.length(); i++) {
            BoardPos pos = wordPlacement.getPositionAt(i);
            if (!hasTileAt(pos)) {
                Tile tile = tilesToPlace.get(j);
                if (tile == Tile.BLANK) {
                    getSquareAt(pos).setBlankTile(wordPlacement.getLetterAt(i));
                } else {
                    setTile(pos, tilesToPlace.get(j));
                }
                j++;
                positions.add(pos);
            }
        }
        return positions;
    }

    /**
     * Gets tiles needed from Player's frame in order to fully apply wordPlacement Takes into
     * account what is already on the board and returns a list of necessary tiles. These may not be
     * exactly the tiles that are placed however. Since a tile can be exchanged for a BLANK if
     * necessary.
     *
     * @param wordPlacement specifies what letter and where it is to be placed
     * @return A non-empty list of tiles that have not already been placed.
     * @throws BadWordPlacementException if a differing letter already exists in one of the
     *     destinations, or if no tiles need to be placed.
     */
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

        if (tileList.isEmpty()) {
            throw new BadWordPlacementException(wordPlacement, "Must place atleast one new tile");
        }

        return tileList;
    }

    /**
     * Prints the board in stylised fashion. All squares are seperated using +, / and - characters.
     */
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
