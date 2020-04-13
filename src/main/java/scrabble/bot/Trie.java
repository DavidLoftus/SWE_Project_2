package scrabble.bot;

import java.util.BitSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Trie implements Iterable<String> {

    static class BadForceException extends IllegalStateException {
        public BadForceException(String message) {
            super(message);
        }
    }

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz+";

    private Trie[] children = new Trie[ALPHABET.length()];
    private BitSet endChars = new BitSet(ALPHABET.length());

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

    private void addEnd(char c) {
        int i = charToIndex(c);
        endChars.set(i);
    }

    private boolean isEnd(char c) {
        int i = charToIndex(c);
        return endChars.get(i);
    }

    public Trie add(String s) {
        if (s.isEmpty()) {
            throw new IllegalArgumentException("can't add empty string");
        }

        // Run assertion on validity of chars before mutating trie to ensure operation is
        // transactional.
        s.chars().forEach(c -> charToIndex((char) c));

        Trie subTrie = this;
        for (int i = 0; i < s.length()-1; ++i) {
            subTrie = subTrie.addArc(s.charAt(i));
        }

        subTrie.addEnd(s.charAt(s.length()-1));

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
        // Run assertion on validity of c2 before mutating trie to ensure operation is
        // transactional.
        charToIndex(c2);
        Trie subTrie = addArc(c1);

        subTrie.addEnd(c2);

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
        int i = charToIndex(c);
        if (children[i] == null) {
            throw new NoSuchElementException("char '" + c + "' not in trie.");
        }
        return children[i];
    }

    public Trie get(String s) {
        Trie subTrie = this;
        for (int i = 0; i < s.length(); ++i) {
            subTrie = subTrie.get(s.charAt(i));
        }
        return subTrie;
    }

    public boolean contains(String s) {
        try {
            Trie subTrie = this;
            for (int i = 0; i < s.length() - 1; ++i) {
                subTrie = subTrie.get(s.charAt(i));
            }
            return subTrie.isEnd(s.charAt(s.length() - 1));
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private class StatefulTrieCollector {
        private boolean firstIter = true;
        private int childIdx = 0;
        private StatefulTrieCollector childCollector;

        private void advanceToNextNonNullChild() {
            while (childIdx < children.length && children[childIdx] == null && !endChars.get(childIdx)) {
                childIdx++;
            }
        }

        boolean tryNext(StringBuffer buffer) {
            advanceToNextNonNullChild();

            // Since we are using lazy iterators, we need alot of ugly stateful code.
            // This loop proceeds until we found a result to yield
            // If none is found we escape and return false
            while (childIdx < children.length) {
                char c = indexToChar(childIdx);
                if (firstIter) {
                    firstIter = false;

                    // Append character to buffer
                    // All subsequent calls to tryNext will have this character present
                    // At least until we switch to next character
                    buffer.append(c);

                    if (isEnd(c)) {
                        return true;
                    }
                }

                if (children[childIdx] != null) {
                    if (childCollector == null) {
                        childCollector = children[childIdx].getCollector();
                    }
                    if (childCollector.tryNext(buffer)) {
                        return true;
                    }
                }
                // We are finished with this character, clean up collector to proceed to next state
                buffer.deleteCharAt(buffer.length() - 1);
                childCollector = null;
                firstIter = true;
                childIdx++;
                advanceToNextNonNullChild();
            }
            return false;
        }
    }

    private StatefulTrieCollector getCollector() {
        return new StatefulTrieCollector();
    }

    private class TrieIterator implements Iterator<String> {
        StatefulTrieCollector collector = getCollector();
        StringBuffer buffer = new StringBuffer();

        boolean resultPending = false;

        @Override
        public boolean hasNext() {
            if (!resultPending) {
                resultPending = collector.tryNext(buffer);
            }
            return resultPending;
        }

        @Override
        public String next() {
            assert hasNext();
            resultPending = false;
            return buffer.toString();
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new TrieIterator();
    }
}
