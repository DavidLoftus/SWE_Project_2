package scrabble.bot;

import java.util.Iterator;

public class Trie implements Iterable<String> {

    static class BadForceException extends IllegalStateException {
        public BadForceException(String message) {
            super(message);
        }
    }

    public Trie() {}

    public Trie add(String s) {
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    public Trie addArc(char c) {
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    public Trie addFinalArc() {
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    public Trie forceArc(char c, Trie trie) throws BadForceException {
        // TODO: implement
        throw new UnsupportedOperationException();
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
