package scrabble.bot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
                gaddag.addWord(word);
                dictionaryTrie.add(word);
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

    private Optional<String> findMove() {
        if (board.isFirstPlay()) {
            return findFirstPlay().map(s -> "H8 A " + s);
        }
        return Optional.empty();
    }

    private boolean canMakeString(String words) {
        return new CharMultiSet(words).isSubsetOf(frame);
    }

    private Optional<String> findFirstPlay() {
        var spliterator = dictionaryTrie.wordsWithLetters(frame).spliterator();
        assert StreamSupport.stream(spliterator, false).allMatch(this::canMakeString);
        return StreamSupport.stream(spliterator, false)
                .max(Comparator.comparingInt(String::length));
    }
}
