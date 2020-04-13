package scrabble;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Dictionary implements DictionaryAPI {

    private static final String inputFileName = "csw.txt";
    private Set<String> wordSet = new HashSet<>();

    Dictionary() throws FileNotFoundException {
        File inputFile = new File(inputFileName);
        Scanner in = new Scanner(inputFile);
        while (in.hasNextLine()) {
            String word = in.nextLine().toUpperCase();
            wordSet.add(word);
        }
        in.close();
    }

    public boolean areWords(ArrayList<Word> words) {
        for (Word word : words) {
            if (!wordSet.contains(word.toString().toUpperCase())) {
                return false;
            }
        }
        return true;
    }
}
