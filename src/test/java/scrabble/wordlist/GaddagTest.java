package scrabble.wordlist;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GaddagTest {

    private static Sowpods sowpods;
    private static Set<String> smallerSet;
    private static Gaddag gaddag;


    @BeforeAll
    static void initWordList() throws IOException {
        sowpods = new Sowpods();
        smallerSet = new HashSet<>();

        for (String s : sowpods.getWordList()) {
            if (s.length() <= 8) {
                smallerSet.add(s);
            }
        }
        benchMarkGaddagGeneration();
    }

    static Runtime runtime = Runtime.getRuntime();
    static long getUsedMemory() {
        return runtime.totalMemory() - runtime.freeMemory();
    }

    static void benchMarkGaddagGeneration() {
        runtime.gc();
        long memoryBefore = getUsedMemory();
        long timeBefore = System.nanoTime();

        gaddag = new Gaddag(smallerSet);

        long timeAfter = System.nanoTime();

        runtime.gc();
        long memoryAfterGC = getUsedMemory();

        long memoryUsed = memoryAfterGC - memoryBefore;
        long timeDiff = timeAfter - timeBefore;

        System.out.printf("Used memory: %.3f MB\n", memoryUsed/1024./1024.);
        System.out.printf("Time taken: %.3f ms.\n", timeDiff/1000.0/1000.0);
    }

    @ParameterizedTest
    @ValueSource(strings = {"o", "i", "u", "a", "e"})
    void complete(String middle) {

        for (Gaddag.Join join : gaddag.complete(middle)) {
            assertEquals(middle, join.middle);

            String word = join.left + join.middle + join.right;
            if (!sowpods.isValidWord(word)) {
                fail("Generated word: \"" + word + "\" is not in sowpods list.");
            }
        }

    }
}