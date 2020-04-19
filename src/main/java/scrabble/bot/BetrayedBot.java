package scrabble.bot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
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
    private int poolSize = 100;
    private int lastPlayLetterCount;

    // Persistent decision tree state
    private boolean hasSetName = false;
    private boolean doesOpponentChallenge = false;
    private boolean needToUpdatePool = true;
    private boolean shouldChallenge = false;

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
            return "NAME BetrayedBot" + player.getPrintableId();
        }

        if (shouldChallenge) {
            shouldChallenge = false;
            return "CHALLENGE";
        }

        return findMove().or(this::tryExchange).orElse("PASS");
    }

    private Optional<String> tryExchange() {
        if (needToUpdatePool) {
            return Optional.of("POOL");
        }
        if (poolSize > 0) {
            String letters = frame.getLetters(false);
            int toExchange = Math.min(letters.length(), poolSize);
            return Optional.of("EXCHANGE " + letters.substring(0, toExchange));
        }
        return Optional.empty();
    }

    private static final Pattern poolResponseRegex = Pattern.compile("Pool has (\\d+) tiles");

    private void parseLogs() {
        List<String> newLogs = log.getLatestLogs(true);

        AtomicBoolean myCommand = new AtomicBoolean(hasSetName);
        var it = newLogs.spliterator();
        while (it.tryAdvance(
                line -> {
                    if (line.startsWith("> ")) {
                        String command = line.substring(2).toUpperCase();
                        if (command.matches(
                                "[A-O](\\d){1,2}( )+[A,D]( )+([A-Z]){1,17}(( )+([A-Z]){1,2})?")) {
                            it.tryAdvance(
                                    message -> {
                                        if (!message.startsWith("Error:")) handlePlacement(command, myCommand.get());
                                    });
                        }
                        if (command.equals("CHALLENGE")) {
                            it.tryAdvance(
                                    message -> {
                                        if (message.startsWith("Challenge success")) {
                                            handleChallenge();
                                            if (!myCommand.get()) {
                                                doesOpponentChallenge = true;
                                            }
                                        }
                                    });
                        }
                        if (command.equals("EXCHANGE")) {
                            it.tryAdvance(
                                    message -> {
                                        if (message.startsWith("Error:")) needToUpdatePool = true;
                                    });
                        }
                        if (command.equals("POOL")) {
                            it.tryAdvance(
                                    message -> {
                                        Matcher matcher = poolResponseRegex.matcher(message);
                                        if (matcher.find()) {
                                            poolSize = Integer.parseInt(matcher.group(1));
                                            needToUpdatePool = false;
                                        }
                                    });
                        }
                    } else if (line.matches(".*'s turn:")) {
                        myCommand.set(false);
                    }
                })) ;
    }

    private void handlePlacement(String command, boolean myCommand) {
        Word move = parsePlay(command);
        lastPlayLetterCount = (int) eachPosition(move).filter(x -> !hasTileAt(x)).count();
        poolSize -= lastPlayLetterCount;
        boardCache.place(fakeFrame, move);

        if (!myCommand) {
            if (!dictionary.areWords(boardCache.getAllWords(move)))
                shouldChallenge = true;
        }
    }

    private void handleChallenge() {
        boardCache.pickupLatestWord();
        poolSize += lastPlayLetterCount;
        lastPlayLetterCount = 0;
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

    private Stream<Coordinates> eachPosition(Word word) {
        int row = word.getFirstRow(), column = word.getFirstColumn();
        if (word.isHorizontal()) {
            return IntStream.range(column, column + word.length())
                    .mapToObj(i -> new Coordinates(row, i));
        } else {
            return IntStream.range(row, word.getFirstRow() + word.length())
                    .mapToObj(i -> new Coordinates(i, column));
        }
    }

    private boolean isInBounds(Coordinates coord) {
        return coord.getRow() >= 0
                && coord.getRow() < 15
                && coord.getCol() >= 0
                && coord.getCol() < 15;
    }

    private boolean hasTileAt(Coordinates coord) {
        return isInBounds(coord)
                && boardCache.getSquare(coord.getRow(), coord.getCol()).isOccupied();
    }

    private Optional<Word> getEmptyNeighbourAt(Coordinates coord, boolean isHorizontal) {
        int row = coord.getRow(), col = coord.getCol();
        int rowInc = getRowIncrement(isHorizontal);
        int columnInc = getRowIncrement(isHorizontal);

        Coordinates leftNeighbour = new Coordinates(row - columnInc, col - rowInc);
        Coordinates rightNeighbour = new Coordinates(row + columnInc, col + rowInc);

        if (hasTileAt(leftNeighbour) || hasTileAt(rightNeighbour)) {
            return Optional.empty();
        } else {
            char letter = boardCache.getSquare(row, col).getTile().getLetter();
            Word word = new Word(row, col, !isHorizontal, String.valueOf(letter));

            return Optional.of(word);
        }
    }

    private Stream<Word> getAllNeighbours(Word word) {
        Stream.Builder<Word> builder = Stream.builder();
        builder.add(word);

        eachPosition(word)
                .map(coord -> getEmptyNeighbourAt(coord, word.isHorizontal()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(builder);

        return builder.build();
    }

    private boolean checkValidPlacement(Word word) {
        boolean inBounds = eachPosition(word).allMatch(this::isInBounds);
        if (inBounds && board.isLegalPlay(fakeFrame, word)) {
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

    private int getScore(Word word) {
        boardCache.place(fakeFrame, word);
        int points = boardCache.getAllPoints(boardCache.getAllWords(word));
        boardCache.pickupLatestWord();
        return points;
    }

    private Word wordFromJoin(Word word, Gaddag.Join join) {
        int row = word.getFirstRow(), col = word.getFirstColumn();
        int leftSize = join.getLeft().length();

        if (word.isHorizontal()) {
            col -= leftSize;
        } else {
            row -= leftSize;
        }

        return new Word(row, col, word.isHorizontal(), join.getWord());
    }

    private Stream<Word> findCompletions(Word word) {
        return gaddag.findWords(word.getLetters(), frame).map(join -> wordFromJoin(word, join));
    }

    private Optional<Word> findFirstPlay() {
        return findCompletions(new Word(7, 7, true, ""))
                .filter(word -> word.getColumn() + word.length() > 7)
                .max(Comparator.comparingInt(this::getScore));
    }
}
