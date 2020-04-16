package scrabble.bot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import scrabble.*;

public class BetrayedBot implements BotAPI {

    private Set<String> wordList = loadWordList();
    private Gaddag gaddag = new Gaddag();
    private Trie dictionaryTrie = new Trie();

    private PlayerAPI player;
    private OpponentAPI opponent;
    private BoardAPI board;
    private UserInterfaceAPI userInterface;
    private DictionaryAPI dictionary;

    // Cached info
    private CharMultiSet frame;
    private LogCache log;
    private Board boardCache;

    // Persistent decision tree state
    private boolean hasSetName = false;
    private boolean doesOpponentChallenge = false;

    // Frame used for placing any word on boardCache
    private Frame fakeFrame =
            new Frame() {
                public int size() {
                    return MAX_TILES;
                }

                public boolean isEmpty() {
                    return false;
                }

                public boolean isFull() {
                    return true;
                }

                public boolean isAvailable(String letters) {
                    return true;
                }

                // remove precondition: isAvailable(letters) is true
                public void removeTile(Tile tile) {}

                // remove precondition: isAvailable(letters) is true
                public void removeTiles(ArrayList<Tile> tiles) {}

                // getTile precondition: isAvailable(letters) is true
                public Tile getTile(Character letter) {
                    return new Tile(letter);
                }

                // remove precondition: isAvailable(letters) is true
                public ArrayList<Tile> getTiles(String letters) {
                    return letters.chars()
                            .mapToObj(c -> new Tile((char) c))
                            .collect(Collectors.toCollection(ArrayList::new));
                }
            };

    public BetrayedBot(
            PlayerAPI me,
            OpponentAPI opponent,
            BoardAPI board,
            UserInterfaceAPI ui,
            DictionaryAPI dictionary) {
        this.player = me;
        this.opponent = opponent;
        this.board = board;
        this.userInterface = ui;
        this.dictionary = dictionary;

        this.log = new LogCache(ui);
        this.boardCache = new Board();

        for (String word : wordList) {
            if (word.length() <= 8) {
                String upperWord = word.toUpperCase();
                gaddag.addWord(upperWord);
                dictionaryTrie.add(upperWord);
            }
        }
    }

    @Override
    public String getCommand() {
        updateState();
        parseLogs();

        if (!hasSetName) {
            hasSetName = true;
            return "NAME BetrayedBot";
        }

        String move = findMove().orElse("PASS");
        String frame = player.getFrameAsString();
        return move;
    }

    private void parseLogs() {
        List<String> newLogs = log.getLatestLogs(true);

        AtomicBoolean myCommand = new AtomicBoolean(hasSetName);
        var it = newLogs.spliterator();
        while (it.tryAdvance(
                line -> {
                    if (line.startsWith("> ")) {
                        String command = line.substring(2);
                        if (command.matches(
                                "[A-O](\\d){1,2}( )+[A,D]( )+([A-Z]){1,17}(( )+([A-Z]){1,2})?")) {
                            it.tryAdvance(
                                    message -> {
                                        if (!message.startsWith("Error:")) {
                                            Word move = parsePlay(command);
                                            boardCache.place(fakeFrame, move);
                                        }
                                    });
                        }
                        if (command.equalsIgnoreCase("CHALLENGE")) {
                            it.tryAdvance(
                                    message -> {
                                        if (message.startsWith("Challenge success")) {
                                            boardCache.pickupLatestWord();
                                            if (!myCommand.get()) {
                                                doesOpponentChallenge = true;
                                            }
                                        }
                                    });
                        }
                    } else if (line.matches("'s turn:")) {
                        myCommand.set(false);
                    }
                })) ;
    }

    private Word parsePlay(String command) {
        // this converts the play command into a scrabble.Word
        String[] parts = command.split("( )+");
        String gridText = parts[0];
        int column = ((int) gridText.charAt(0)) - ((int) 'A');
        String rowText = parts[0].substring(1);
        int row = Integer.parseInt(rowText) - 1;
        String directionText = parts[1];
        boolean isHorizontal = directionText.equals("A");
        String letters = parts[2];
        Word word;
        if (parts.length == 3) {
            word = new Word(row, column, isHorizontal, letters);
        } else {
            String designatedBlanks = parts[3];
            word = new Word(row, column, isHorizontal, letters, designatedBlanks);
        }
        return word;
    }

    private void updateState() {
        String frameArrayStr = player.getFrameAsString();
        String[] frameChars = frameArrayStr.substring(1, frameArrayStr.length() - 1).split(", ");
        frame = new CharMultiSet(String.join("", frameChars));
    }

    private Set<String> loadWordList() {
        try (BufferedReader reader = new BufferedReader(new FileReader("csw.txt"))) {
            return reader.lines().collect(Collectors.toSet());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String makePlaceCommand(Word word) {
        CharMultiSet frameCopy = new CharMultiSet(frame);

        StringBuilder commands = new StringBuilder();
        commands.append((char) ('A' + word.getFirstColumn())).append(word.getFirstRow() + 1);
        commands.append(' ').append(word.isHorizontal() ? 'A' : 'D').append(' ');

        StringBuilder blankDesignation = new StringBuilder();

        int rowInc = getRowIncrement(word.isHorizontal());
        int columnInc = getColumnIncrement(word.isHorizontal());

        int row = word.getFirstRow(), column = word.getFirstColumn();
        for (int i = 0; i < word.length(); ++i) {
            Square square = boardCache.getSquare(row, column);
            if (!square.isOccupied()) {
                char c = word.getLetter(i);
                char toPlace = frameCopy.take(c);
                commands.append(toPlace);
                if (toPlace == '_') {
                    blankDesignation.append(c);
                }
            } else {
                commands.append(square.getTile().getLetter());
            }
            row += rowInc;
            column += columnInc;
        }

        if (blankDesignation.length() > 0) {
            commands.append(' ').append(blankDesignation);
        }

        return commands.toString();
    }

    private int getRowIncrement(boolean isHorizontal) {
        return isHorizontal ? 0 : 1;
    }

    private int getColumnIncrement(boolean isHorizontal) {
        return isHorizontal ? 1 : 0;
    }

    private void forEachPosition(Word word, Consumer<Coordinates> consumer) {
        int row = word.getFirstRow(), column = word.getFirstColumn();

        int rowInc = getRowIncrement(word.isHorizontal());
        int columnInc = getColumnIncrement(word.isHorizontal());

        for (int i = 0; i < word.length(); ++i) {
            consumer.accept(new Coordinates(row, column));

            row += rowInc;
            column += columnInc;
        }
    }

    private boolean isInBounds(Coordinates coord) {
        return coord.getRow() >= 0
                && coord.getRow() < 15
                && coord.getCol() >= 0
                && coord.getCol() < 15;
    }

    private Stream<Word> getAllNeighbours(Word word) {
        Stream.Builder<Word> builder = Stream.builder();
        builder.add(word);

        int rowInc = getRowIncrement(word.isHorizontal());
        int columnInc = getRowIncrement(word.isHorizontal());

        forEachPosition(
                word,
                coord -> {
                    int row = coord.getRow(), col = coord.getCol();
                    Coordinates leftNeighbour = new Coordinates(row - columnInc, col - rowInc);
                    Coordinates rightNeighbour = new Coordinates(row + columnInc, col + rowInc);
                    if ((!isInBounds(leftNeighbour)
                            || !boardCache
                            .getSquare(
                                    leftNeighbour.getRow(), leftNeighbour.getCol())
                            .isOccupied())
                            && (!isInBounds(rightNeighbour)
                            || !boardCache
                            .getSquare(
                                    rightNeighbour.getRow(),
                                    rightNeighbour.getCol())
                            .isOccupied())) {
                        builder.add(
                                new Word(
                                        coord.getRow(),
                                        coord.getCol(),
                                        !word.isHorizontal(),
                                        "" + boardCache.getSquare(row, col).getTile().getLetter()));
                    }
                });
        return builder.build();
    }

    private boolean checkValidPlacement(Word word) {
        AtomicBoolean inBounds = new AtomicBoolean(true);
        forEachPosition(
                word,
                pos -> {
                    if (!isInBounds(pos)) {
                        inBounds.set(false);
                    }
                });
        if (inBounds.get()
                && board.isLegalPlay(fakeFrame, word)
                && dictionary.areWords(new ArrayList<>(List.of(word)))) {
            boardCache.place(fakeFrame, word);

            boolean good = dictionary.areWords(boardCache.getAllWords(word));

            boardCache.pickupLatestWord();

            return good;
        }
        return false;
    }

    private Optional<String> findMove() {
        if (board.isFirstPlay()) {
            return findFirstPlay().map(this::makePlaceCommand);
        } else {
            Map<String, Word> words = gatherWordsOnBoard();
            return words.values().stream()
                    .flatMap(this::getAllNeighbours)
                    .flatMap(this::findCompletions)
                    .filter(this::checkValidPlacement)
                    .max(Comparator.comparingInt(this::getScore))
                    .map(this::makePlaceCommand);
        }
    }

    private Map<String, Word> gatherWordsOnBoard() {
        Map<String, Word> words = new HashMap<>();
        for (int i = 0; i < 15; ++i) {
            for (int j = 0; j < 15; ++j) {
                Square square = boardCache.getSquare(i, j);
                if (square.isOccupied()) {
                    Word horizontalWord = new Word(i, j, true, "" + square.getTile().getLetter());
                    for (Word word : boardCache.getAllWords(horizontalWord)) {
                        String startPos =
                                String.format("%c%d", 'A' + word.getColumn(), word.getRow() + 1);
                        words.put(startPos, word);
                    }
                    Word verticalWord = new Word(i, j, false, "" + square.getTile().getLetter());
                    for (Word word : boardCache.getAllWords(verticalWord)) {
                        String startPos =
                                String.format("%c%d", 'A' + word.getColumn(), word.getRow() + 1);
                        words.put(startPos, word);
                    }
                }
            }
        }
        return words;
    }

    private boolean canMakeString(String words) {
        return new CharMultiSet(words).isSubsetOf(frame);
    }

    private int getScore(Word word) {
        boardCache.place(fakeFrame, word);
        int points = boardCache.getAllPoints(boardCache.getAllWords(word));
        boardCache.pickupLatestWord();
        return points;
    }

    private Stream<Word> findCompletions(Word word) {
        return gaddag.findWords(word.getLetters(), frame)
                .map(
                        join -> {
                            if (word.isHorizontal()) {
                                return new Word(
                                        word.getFirstRow(),
                                        word.getFirstColumn() - join.getLeft().length(),
                                        word.isHorizontal(),
                                        join.getWord());
                            } else {
                                return new Word(
                                        word.getFirstRow() - join.getLeft().length(),
                                        word.getFirstColumn(),
                                        word.isHorizontal(),
                                        join.getWord());
                            }
                        });
    }

    private Optional<Word> findFirstPlay() {
        var spliterator = dictionaryTrie.wordsWithLetters(frame).spliterator();
        return StreamSupport.stream(spliterator, false)
                .map(s -> new Word(7, 7, true, s))
                .max(Comparator.comparingInt(this::getScore));
    }
}
