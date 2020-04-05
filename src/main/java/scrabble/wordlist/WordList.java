package scrabble.wordlist;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.zip.GZIPInputStream;

/**
 * WordList holds a set of all valid words according to the SOWPODS scrabble dictionary. This
 * dictionary is stored as a resource in the jar and is loaded during construction of the class.
 */
public class WordList {

    private Set<String> wordList;

    /**
     * Constructs the WordList by loading the sowpods.txt.gz file from resources and filling
     * `wordList` with contents.
     */
    public WordList() {
        InputStream fileStream = WordList.class.getResourceAsStream("sowpods.txt.gz");
        try {
            this.wordList = readWordList(new GZIPInputStream(fileStream));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads word list from input stream to produce a set of words.
     *
     * @param inputStream decompressed input stream containing lines of dictionary words
     * @return set containing all words from `inputStream`.
     */
    private Set<String> readWordList(InputStream inputStream) {
        Set<String> wordList = new HashSet<>();
        try (Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNext()) {
                wordList.add(scanner.next().toUpperCase());
            }
        }
        return wordList;
    }

    /**
     * Checks if the given word is valid. This function is case insensitive.
     *
     * @param word to query for
     * @return true if word is in dictionary otherwise false.
     */
    public boolean isValidWord(String word) {
        return wordList.contains(word.toUpperCase());
    }
}
