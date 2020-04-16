package scrabble.bot;

import java.util.Arrays;
import java.util.Iterator;

public class CharMultiSet implements Iterable<Character> {

    public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private int wildCards = 0;
    private int[] counts = new int[ALPHABET.length()];

    public CharMultiSet(String s) {
        s.toLowerCase()
                .chars()
                .forEach(
                        c -> {
                            if (c == '_') {
                                wildCards++;
                            } else {
                                counts[charToIndex((char) c)]++;
                            }
                        });
    }

    public CharMultiSet(CharMultiSet other) {
        this.wildCards = other.wildCards;
        this.counts = Arrays.copyOf(other.counts, ALPHABET.length());
    }

    private int charToIndex(char c) {
        return Character.toUpperCase(c) - 'A';
    }

    public boolean isSubsetOf(CharMultiSet other) {
        int wildCardsUsed = 0;
        for (int i = 0; i < ALPHABET.length(); ++i) {
            if (counts[i] > other.counts[i]) {
                int delta = counts[i] - other.counts[i];
                wildCardsUsed += delta;
                if (wildCardsUsed > other.wildCards) {
                    return false;
                }
            }
        }
        return true;
    }

    public char take(char c) {
        if (c == '_') {
            assert wildCards > 0;
            wildCards++;
        } else {
            int i = charToIndex(c);
            if (counts[i] > 0) {
                counts[i]--;
            } else if (wildCards > 0) {
                wildCards--;
                return '_';
            } else {
                throw new IllegalArgumentException(
                        "can't take character " + c + " as none are present");
            }
        }
        return c;
    }

    public void add(char c) {
        if (c == '_') {
            wildCards++;
        } else {
            int i = charToIndex(c);
            counts[i]++;
        }
    }

    public boolean has(char c) {
        int i = charToIndex(c);
        return counts[i] > 0 || wildCards > 0;
    }

    public String getLetters(boolean includeWildcards) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < ALPHABET.length(); ++i) {
            for (int j = 0; j < counts[i]; ++j) {
                builder.append(ALPHABET.charAt(i));
            }
        }
        if (includeWildcards) {
            for (int j = 0; j < wildCards; ++j) {
                builder.append('_');
            }
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");

        boolean first = true;
        for (int i = 0; i < ALPHABET.length(); ++i) {
            if (counts[i] > 0) {
                if (first) {
                    first = false;
                } else {
                    sb.append(", ");
                }
                sb.append('\'').append(ALPHABET.charAt(i)).append("': ").append(counts[i]);
            }
        }

        sb.append('}');
        return sb.toString();
    }

    @Override
    public Iterator<Character> iterator() {
        return new Iterator<>() {
            int i = 0;
            int j = 0;
            int k = 0;

            @Override
            public boolean hasNext() {
                while (i < counts.length && j < counts[i]) {
                    ++i;
                    j = 0;
                }
                return i < counts.length || k < wildCards;
            }

            @Override
            public Character next() {
                assert hasNext();
                if (i < counts.length) {
                    --j;
                    return (char) ('A' + i);
                } else {
                    --k;
                    return '_';
                }
            }
        };
    }
}
