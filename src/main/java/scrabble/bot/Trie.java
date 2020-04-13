package scrabble.bot;

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
        // Run assertion on validity of chars before mutating trie to ensure operation is
        // transactional.
        s.chars().forEach(c -> charToIndex((char) c));

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
        // Run assertion on validity of c2 before mutating trie to ensure operation is
        // transactional.
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
            Trie subTrie = get(s);
            return subTrie.isEnd;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private class StatefulTrieCollector {
        private boolean hasCheckedIsEnd = false;
        private int childIdx = 0;
        private StatefulTrieCollector childCollector;

        private void advanceToNextNonNullChild() {
            while (childIdx < children.length && children[childIdx] == null) {
                childIdx++;
            }
        }

        boolean tryNext(StringBuffer buffer) {
            // First yielded result should be the smallest string
            // The rest should be in alphabetical order
            if (!hasCheckedIsEnd) {
                hasCheckedIsEnd = true;
                if (isEnd) {
                    return true;
                }
            }

            advanceToNextNonNullChild();

            // Since we are using lazy iterators, we need alot of ugly stateful code.
            // This loop proceeds until we found a result to yield
            // If none is found we escape and return false
            while (childIdx < children.length) {
                if (childCollector == null) {
                    // Append character to buffer
                    // All subsequent calls to tryNext will have this character present
                    // At least until childCollect.tryNext() returns false
                    buffer.append(indexToChar(childIdx));
                    childCollector = children[childIdx].getCollector();
                }
                if (childCollector.tryNext(buffer)) {
                    return true;
                }
                // Collector is finished, clean up collector to proceed to next state
                buffer.deleteCharAt(buffer.length() - 1);
                childCollector = null;
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
