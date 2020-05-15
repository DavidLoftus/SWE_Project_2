package scrabble.bot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import scrabble.*;

public class BetrayedBot implements BotAPI {

    private Gaddag gaddag = new Gaddag();

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

        Set<String> wordList = loadWordList();
        for (String word : wordList) {
            //            if (word.length() <= 15) {
            String upperWord = word.toUpperCase();
            gaddag.addWord(upperWord);
            //            }
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
                                        if (!message.startsWith("Error:"))
                                            handlePlacement(command, myCommand.get());
                                    });
                        }
                        if (command.equals("CHALLENGE")) {
                            it.tryAdvance(
                                    message -> {
                                        if (message.startsWith("Challenge success")) {
                                            handleChallenge();
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
            if (!dictionary.areWords(boardCache.getAllWords(move))) shouldChallenge = true;
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

        int offset = commands.length();
        commands.append(word.getLetters());

        List<Integer> blankPositions = new ArrayList<>();

        int rowInc = getRowIncrement(word.isHorizontal());
        int columnInc = getColumnIncrement(word.isHorizontal());

        IntStream.range(0, word.length())
                .boxed()
                .sorted(
                        Comparator.comparingInt(
                                i -> {
                                    int row = word.getFirstRow() + i * rowInc;
                                    int column = word.getFirstColumn() + i * columnInc;
                                    Square square = boardCache.getSquare(row, column);
                                    return square.isOccupied()
                                            ? 0
                                            : 3 * (square.getWordMultiplier() - 1)
                                                    + square.getLetterMuliplier()
                                                    - 1;
                                }))
                .forEachOrdered(
                        i -> {
                            int row = word.getFirstRow() + i * rowInc;
                            int column = word.getFirstColumn() + i * columnInc;
                            Square square = boardCache.getSquare(row, column);
                            if (!square.isOccupied()) {
                                char c = word.getLetter(i);
                                char toPlace = frameCopy.take(c);
                                commands.setCharAt(offset + i, toPlace);
                                if (toPlace == '_') {
                                    blankPositions.add(i);
                                }
                            } else {
                                commands.setCharAt(offset + i, square.getTile().getLetter());
                            }
                        });

        if (blankPositions.size() > 0) {
            commands.append(' ');
            blankPositions.stream().sorted().map(word::getLetter).forEachOrdered(commands::append);
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

    private boolean checkValidPlacement(Word word) {
        boolean inBounds = eachPosition(word).allMatch(this::isInBounds);
        return inBounds && board.isLegalPlay(fakeFrame, word);
    }

    private boolean checkAllWordsValid(Word word) {
        boardCache.place(fakeFrame, word);

        boolean good = dictionary.areWords(boardCache.getAllWords(word));

        boardCache.pickupLatestWord();

        return good;
    }

    private Optional<Word> bestInvalidWord(Set<Word> words) {
        return words.stream()
                .flatMap(this::findCompletions)
                .filter(this::checkValidPlacement)
                .max(Comparator.comparingInt(this::getScore));
    }

    private Optional<String> findMove() {
        if (board.isFirstPlay()) {
            return findFirstPlay().map(this::makePlaceCommand);
        } else {
            Set<Word> words = gatherWordsOnBoard();
            Optional<Word> bestWord =
                    words.stream()
                            .flatMap(this::findCompletions)
                            .filter(this::checkValidPlacement)
                            .filter(this::checkAllWordsValid)
                            .max(Comparator.comparingInt(this::getScore));
            return bestWord.map(this::makePlaceCommand);
        }
    }

    private Set<Word> gatherWordsOnBoard() {
        Set<Word> words = new HashSet<>();
        for (int i = 0; i < 15; ++i) {
            for (int j = 0; j < 15; ++j) {
                Square square = boardCache.getSquare(i, j);
                if (hasTileAt(new Coordinates(i, j))) {
                    Word horizontalWord = getWordSpan(i, j, true);
                    if (words.add(horizontalWord)) {
                        addEdges(words, horizontalWord);
                    }

                    Word verticalWord = getWordSpan(i, j, false);
                    if (words.add(verticalWord)) {
                        addEdges(words, verticalWord);
                    }
                }
            }
        }
        return words;
    }

    private void addEdgeIfEmpty(Set<Word> words, int i, int j, boolean isHorizontal) {
        if (i >= 0 && j >= 0 && i < 15 && j < 15 && !boardCache.getSquare(i, j).isOccupied()) {
            words.add(new Word(i, j, isHorizontal, ""));
        }
    }

    private void addEdges(Set<Word> words, Word word) {
        int row = word.getFirstRow();
        int col = word.getFirstColumn();
        int rowInc = getRowIncrement(word.isHorizontal());
        int colInc = getColumnIncrement(word.isHorizontal());

        addEdgeIfEmpty(words, row - rowInc, col - colInc, !word.isHorizontal());
        addEdgeIfEmpty(
                words,
                row + rowInc * word.length(),
                col + colInc * word.length(),
                !word.isHorizontal());

        addEdgeIfEmpty(words, row - colInc, col - rowInc, !word.isHorizontal());
        addEdgeIfEmpty(words, row + colInc, col + rowInc, !word.isHorizontal());
    }

    private Word getWordSpan(int row, int column, boolean isHorizontal) {
        int startRow = row, startCol = column;

        int rowInc = getRowIncrement(isHorizontal), colInc = getColumnIncrement(isHorizontal);

        while (hasTileAt(new Coordinates(startRow - rowInc, startCol - colInc))) {
            startRow -= rowInc;
            startCol -= colInc;
        }

        StringBuilder wordLetters = new StringBuilder();

        int length = 0;
        while (true) {
            Coordinates pos =
                    new Coordinates(startRow + length * rowInc, startCol + length * colInc);
            if (!hasTileAt(pos)) {
                break;
            }
            wordLetters.append(
                    boardCache.getSquare(pos.getRow(), pos.getCol()).getTile().getLetter());
            length++;
        }

        return new Word(startRow, startCol, isHorizontal, wordLetters.toString());
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
        Predicate<String> p =
                prefix -> {
                    Gaddag.Join join = new Gaddag.Join(word.getLetters(), prefix);
                    Word newWord = wordFromJoin(word, join);
                    return canPlace(newWord);
                };

        return gaddag.findWords(word.getLetters(), p).map(join -> wordFromJoin(word, join));
    }

    private boolean canPlace(Word newWord) {
        if (!eachPosition(newWord).allMatch(this::isInBounds)) {
            return false;
        }
        CharMultiSet frame = new CharMultiSet(this.frame);

        int rowInc = getRowIncrement(newWord.isHorizontal()),
                colInc = getColumnIncrement(newWord.isHorizontal());
        int row = newWord.getFirstRow(), col = newWord.getFirstColumn();
        for (int i = 0; i < newWord.length(); ++i) {
            char c = newWord.getLetter(i);
            Square square = boardCache.getSquare(row, col);
            if (square.isOccupied()) {
                if (square.getTile().getLetter() != c) {
                    return false;
                }
            } else {
                if (!frame.has(c)) {
                    return false;
                }
                frame.take(c);
            }
            row += rowInc;
            col += colInc;
        }

        return true;
    }

    private Optional<Word> findFirstPlay() {
        return findCompletions(new Word(7, 7, true, ""))
                .filter(word -> word.getColumn() + word.length() > 7)
                .filter(this::checkValidPlacement)
                .filter(word -> new CharMultiSet(word.getLetters()).isSubsetOf(frame))
                .max(Comparator.comparingInt(this::getScore));
    }
}
