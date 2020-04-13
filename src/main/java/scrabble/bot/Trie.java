package scrabble.bot;

import java.util.Iterator;

public class Trie implements Iterable<String> {

    static class BadForceException extends IllegalStateException {
        public BadForceException(String message) {
            super(message);
        }
    }

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz+";

    private Trie[] children = new Trie[ALPHABET.length()];
    private boolean isEnd = false;

    private static int charToIndex(char c) {
        int i = ALPHABET.indexOf(c);
        if (i == -1) {
            throw new IllegalArgumentException("invalid char: " + c);
        }
        return i;
    }

    private static char indexToChar(int i) {
        return ALPHABET.charAt(i);
    }

    public Trie add(String s) {
        // Run assertion on validity of chars before mutating trie to ensure operation is transactional.
        s.chars().forEach(c -> charToIndex((char)c));

        Trie subTrie = this;
        for (int i = 0; i < s.length(); ++i) {
            subTrie = subTrie.addArc(s.charAt(i));
        }
        subTrie.isEnd = true;
        return subTrie;
    }

    public Trie addArc(char c) {
        int i = charToIndex(c);
        if (children[i] == null) {
            children[i] = new Trie();
        }
        return children[i];
    }

    public Trie addFinalArc(char c1, char c2) {
        // Run assertion on validity of c2 before mutating trie to ensure operation is transactional.
        charToIndex(c2);
        Trie subTrie = addArc(c1);
        Trie nestedSubTrie = subTrie.addArc(c2);
        nestedSubTrie.isEnd = true;
        return subTrie;
    }

    public Trie forceArc(char c, Trie trie) throws BadForceException {
        int i = charToIndex(c);
        if (children[i] == null) {
            children[i] = trie;
        } else if (children[i] != trie) {
            throw new BadForceException("Tried to force arc '" + c + "' but node already exists.");
        }
        return children[i];
    }

    public Trie get(char c) {
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    public Trie get(String s) {
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    public boolean contains(String s) {
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<String> iterator() {
        // TODO: implement
        throw new UnsupportedOperationException();
    }
}
