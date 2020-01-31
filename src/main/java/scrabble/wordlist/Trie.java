package scrabble.wordlist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Trie implements Iterable<String> {

    private static final int NUM_CHARS = 27;

    private Trie[] children = new Trie[NUM_CHARS];
    private boolean isEnd = false;

    private static int charToIndex(char c) {
        if (c == '+') {
            return 26;
        }
        if (!Character.isLowerCase(c)) {
            throw new IllegalArgumentException("characters must be lowercase letters");
        }
        return c - 'a';
    }

    private static char indexToChar(int i) {
        return "abcdefghijklmnopqrstuvwxyz+".charAt(i);
    }

    private void add(String s, int i) {
        if (i == s.length()) {
            isEnd = true;
            return;
        } else if (i > s.length()) {
            throw new IllegalArgumentException("i greater than length of string");
        }

        int idx = charToIndex(s.charAt(i));
        if (children[idx] == null) {
            children[idx] = new Trie();
        }

        children[idx].add(s, i+1);
    }

    public void add(String s) {
        add(s, 0);
    }

    private Trie get(String s, int i) {
        if (i == s.length()) {
            return this;
        } else if (i > s.length()) {
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
        try {
            Trie trie = get(word);
            return trie.isEnd;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private class TrieIterator implements Iterator<String> {
        boolean retEnd = false;
        private int currentChild = 0;
        private Iterator<String> childIterator;

        public TrieIterator() {
            advanceToNext();
        }

        private void advanceToNext() {
            childIterator = null;
            while (currentChild < NUM_CHARS) {
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

        @Override
        public boolean hasNext() {
            return (isEnd && !retEnd) || childIterator != null;
        }

        @Override
        public String next() {

            if (isEnd && !retEnd) {
                retEnd = true;
                return "";
            }

            String ret = childIterator.next();
            if (!childIterator.hasNext()) {
                advanceToNext();
            }
            return indexToChar(currentChild) + ret;
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
