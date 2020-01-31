package scrabble.wordlist;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

public class Gaddag {

    private Trie rootTrie = new Trie();

    public Gaddag(Set<String> wordList) {
        for (String word : wordList) {
            addEachPath(word);
        }
    }

    private void addEachPath(String word) {
        for (int i = 1; i < word.length(); ++i) {
            String left = word.substring(0, i);
            String right = word.substring(i);

            StringBuilder sb = new StringBuilder();
            sb.append(left).reverse();

            if (!right.isEmpty()) {
                sb.append('+').append(right);
            }

            rootTrie.add(sb.toString());
        }
    }

    public List<String> complete(String s) {
        String sReversed = new StringBuilder(s).reverse().toString();

        try {
            Trie child = rootTrie.get(sReversed);

            return child.collectAll();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

}
