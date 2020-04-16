package scrabble.bot;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GaddagTest {

    private static Set<String> wordList = new HashSet<>();

    @BeforeAll
    static void initWordList() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("csw.txt"))) {
            reader.lines()
                    .filter(s -> s.length() < 8)
                    .map(String::toUpperCase)
                    .forEach(wordList::add);
        }
    }

    private Gaddag gaddag;

    @BeforeEach
    void init() {
        this.gaddag = new Gaddag(wordList);
    }

    @Test
    void findWords() {
        AtomicBoolean foundExplain = new AtomicBoolean(false);
        gaddag.findWords("LAI")
                .forEach(
                        join -> {
                            String word = join.getWord();
                            assertTrue(wordList.contains(word), "bad word: " + word);
                            if (word.equals("EXPLAIN")) {
                                foundExplain.set(true);
                            }
                        });
        assertTrue(foundExplain.get());
    }
}
