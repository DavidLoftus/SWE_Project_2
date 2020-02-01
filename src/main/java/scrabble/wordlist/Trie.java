package scrabble.wordlist;

import java.util.*;

public class Trie implements Iterable<String> {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz+";

    private Trie[] children = new Trie[ALPHABET.length()];
    private int endSet = 0;

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
        endSet |= 1 << i;
    }

    private boolean isEnd(char c) {
        int i = charToIndex(c);
        int bit = (endSet >>> i) & 1;
        return bit != 0;
    }

    private Trie addArc(char c) {
        int i = charToIndex(c);
        if (children[i] == null) {
            children[i] = new Trie();
        }
        return children[i];
    }

    private Trie addFinalArc(char c1, char c2) {
        Trie node = addArc(c1);
        node.addEnd(c2);
        return node;
    }

    private void add(String s, int i) {
        if (i == s.length() - 1) {
            addEnd(s.charAt(i));
            return;
        } else if (i >= s.length()) {
            throw new IndexOutOfBoundsException(i);
        }

        Trie child = addArc(s.charAt(i));
        child.add(s, i + 1);
    }

    public void add(String s) {
        add(s, 0);
    }

    private Trie get(String s, int i) {
        if (i == s.length()) {
            return this;
        } else if (i >= s.length()) {
            throw new IllegalArgumentException("i greater than length of string");
        }

        int idx = charToIndex(s.charAt(i));
        if (children[idx] == null) {
            throw new NoSuchElementException("key " + s + " is not in Trie");
        }
        return children[idx].get(s, i+1);
    }

    public Trie get(String s) {
        return get(s, 0);
    }

    public boolean contains(String word) {
        Trie trie = this;
        for (int i = 0; trie != null && i < word.length()-1; ++i) {
            int idx = charToIndex(word.charAt(i));
            trie = trie.children[idx];
        }

        return trie != null && trie.isEnd(word.charAt(word.length()-1));
    }

    private class TrieIterator implements Iterator<String> {
        private int nextSetEndBit = nextSetBit(0);
        private int currentChild = 0;
        private Iterator<String> childIterator;

        public TrieIterator() {
            advanceToNext();
        }

        private void advanceToNext() {
            childIterator = null;
            while (currentChild < children.length) {
                Trie child = children[currentChild];
                if (child != null) {
                    childIterator = child.iterator();
                    if (childIterator.hasNext()) {
                        break;
                    }
                }
                ++currentChild;
            }
        }

        private int nextSetBit(int i) {
            int bitSet = endSet >>> i;
            while (bitSet != 0 && (bitSet & 1) == 0) {
                ++i;
                bitSet >>>= 1;
            }
            if (bitSet == 0) {
                return -1;
            }
            return i;
        }

        @Override
        public boolean hasNext() {
            return nextSetEndBit != -1 || childIterator != null;
        }

        @Override
        public String next() {
            if (nextSetEndBit != -1) {
                String ret = String.valueOf(indexToChar(nextSetEndBit));
                nextSetEndBit = nextSetBit(nextSetEndBit+1);
                return ret;
            }

            String ret = indexToChar(currentChild) + childIterator.next();
            if (!childIterator.hasNext()) {
                ++currentChild;
                advanceToNext();
            }
            return ret;
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new TrieIterator();
    }

    public List<String> collectAll() {
        List<String> list = new ArrayList<>();
        for (String s : this) {
            list.add(s);
        }
        return list;
    }

}
