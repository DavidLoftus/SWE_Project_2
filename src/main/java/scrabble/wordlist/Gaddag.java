package scrabble.wordlist;

import java.util.*;

public class Gaddag {

    private Trie rootTrie = new Trie();

    public Gaddag(Set<String> wordList) {
        for (String word : wordList) {
            try {
                addEachPath(word);
            } catch (Trie.BadForceException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void addEachPath(String word) throws Trie.BadForceException {
        Trie st = rootTrie;

        // Add reverse of word as a path
        for (int i = word.length() - 1; i >= 2; --i) {
            st = st.addArc(word.charAt(i));
        }
        st.addFinalArc(word.charAt(1), word.charAt(0));


        // Add Rev(word[0:l-1])+word[l-1] as path
        st = rootTrie;
        for (int i = word.length() - 2; i >= 0; --i) {
            st = st.addArc(word.charAt(i));
        }
        st = st.addFinalArc('+', word.charAt(word.length()-1));


        // Add rest of the paths
        for (int m = word.length() - 3; m >= 0; --m) {
            final Trie forceSt = st;
            st = rootTrie;

            for (int i = m; i >= 0; --i) {
                st = st.addArc(word.charAt(i));
            }
            st = st.addArc('+');
            st.forceArc(word.charAt(m+1), forceSt);
        }
    }

    public class Join {
        public String left, middle, right;

        public Join(String left, String middle, String right) {
            this.left = left;
            this.middle = middle;
            this.right = right;
        }

        public Join(String middle, String keyRight) {
            this.middle = middle;
            int i = keyRight.indexOf('+');
            if (i != -1) {
                this.left = new StringBuilder(keyRight.substring(0, i)).reverse().toString();
                this.right = keyRight.substring(i+1);
            } else {
                this.left = new StringBuilder(keyRight).reverse().toString();
                this.right = "";
            }
        }

        @Override
        public String toString() {
            return String.format("%s+%s+%s", left, middle, right);
        }
    }

    public List<Join> complete(String s) {
        String sReversed = new StringBuilder(s).reverse().toString();

        try {
            Trie child = rootTrie.get(sReversed);

            List<String> list = child.collectAll();

            List<Join> joinList = new ArrayList<>(list.size());
            for (String rest : list) {
                joinList.add(new Join(s, rest));
            }
            return joinList;
        } catch (NoSuchElementException e) {
            return new ArrayList<>();
        }
    }

}
