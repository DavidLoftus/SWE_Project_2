package scrabble.wordlist;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.zip.GZIPInputStream;

public class WordList {

    private Set<String> wordList;

    public WordList() {
        InputStream fileStream = WordList.class.getResourceAsStream("sowpods.txt.gz");
        try {
            this.wordList = readWordList(new GZIPInputStream(fileStream));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Set<String> readWordList(InputStream inputStream) {
        Set<String> wordList = new HashSet<>();
        try (Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNext()) {
                wordList.add(scanner.next());
            }
        }
        return wordList;
    }

    public boolean isValidWord(String word) {
        return wordList.contains(word);
    }
}
