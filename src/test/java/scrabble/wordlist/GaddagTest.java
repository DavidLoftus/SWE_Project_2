package scrabble.wordlist;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GaddagTest {

    private static Sowpods sowpods;
    private static Set<String> smallerSet;


    @BeforeAll
    static void initWordList() throws IOException {
        sowpods = new Sowpods();
        smallerSet = new HashSet<>();

        for (String s : sowpods.getWordList()) {
            if (s.length() <= 8) {
                smallerSet.add(s);
            }
        }

    }
    static Runtime runtime = Runtime.getRuntime();
    static long getUsedMemory() {
        return runtime.totalMemory() - runtime.freeMemory();
    }

    @Test
    void complete() throws IOException {

        System.out.println("set size: " + smallerSet.size());

        assertTrue(smallerSet.contains("racecard"));

        runtime.gc();
        long memoryBefore = getUsedMemory();

        Gaddag gaddag = new Gaddag(smallerSet);

        long memoryAfter = getUsedMemory();

        runtime.gc();

        long memoryAfterGC = getUsedMemory();

        System.out.printf("Free Memory: %d\nAfter GC: %d\n", memoryAfter - memoryBefore, memoryAfterGC - memoryBefore);

        System.out.println(gaddag.complete("race"));
    }
}