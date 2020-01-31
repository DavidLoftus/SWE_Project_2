package scrabble.wordlist;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.zip.GZIPInputStream;

public class Sowpods {
    private final Set<String> wordList = new HashSet<>();

    public Sowpods() throws IOException {
        InputStream file = Sowpods.class.getResourceAsStream("sowpods.txt.gz");
        GZIPInputStream unzipper = new GZIPInputStream(file);

        Scanner sc = new Scanner(unzipper);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            wordList.add(line);
        }
    }

    public Set<String> getWordList() {
        return Collections.unmodifiableSet(wordList);
    }

    public boolean isValidWord(String word) {
        return wordList.contains(word.toLowerCase());
    }
}
