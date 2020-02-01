package scrabble.wordlist;

import java.util.*;

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

            StringBuilder sb = new StringBuilder(left.length() + right.length() + 1);
            sb.append(left).reverse();

            if (!right.isEmpty()) {
                sb.append('+').append(right);
            }

            rootTrie.add(sb.toString());
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
            return String.format("Join[%s,%s,%s]", left, middle, right);
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
