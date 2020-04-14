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

    // Persistent decision tree state
    private boolean hasSetName = false;
    private boolean doesOpponentChallenge = false;

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

        for (String word : wordList) {
            if (word.length() <= 8) {
                gaddag.addWord(word);
                dictionaryTrie.add(word);
            }
        }
    }

    int turnCount = 0;

    @Override
    public String getCommand() {
        updateState();

        List<String> newLogs = log.getLatestLogs(true);

        if (newLogs.size() >= 2) {
            String line = newLogs.get(1);
            if (line.startsWith("Error: ")) {
                throw new RuntimeException(line);
            }
        }

        System.out.println("=====[Pre Command]=====");

        newLogs.stream().takeWhile(s -> !s.startsWith("> ")).forEach(System.out::println);

        Stream<String> myCommandLogs =
                newLogs.stream().takeWhile(s -> !s.startsWith(opponent.getName() + "'s turn:"));

        System.out.println("========[My Command]=======");
        myCommandLogs.forEach(System.out::println);

        Stream<String> opponentCommandLogs =
                newLogs.stream()
                        .dropWhile(s -> !s.startsWith(opponent.getName() + "'s turn:"))
                        .takeWhile(s -> !s.startsWith(player.getName() + "'s turn:"));

        System.out.println("========[Opponent Command]=======");
        opponentCommandLogs.forEach(System.out::println);

        System.out.println("---------------------");

        if (!hasSetName) {
            hasSetName = true;
            return "NAME BetrayedBot";
        }

        String move = findMove().orElse("PASS");
        String frame = player.getFrameAsString();
        return move;
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
