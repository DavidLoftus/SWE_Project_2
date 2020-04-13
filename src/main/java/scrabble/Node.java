package scrabble;

public class Node {
    private static final int NUM_LETTERS = 26;

    private Node[] children = new Node[NUM_LETTERS];
    private boolean endOfWord;

    Node() {
        for (int i = 0; i < NUM_LETTERS - 1; i++) {
            children[i] = null;
        }
        endOfWord = false;
    }

    private void assertValidChar(char letter) {
        assert Character.isUpperCase(letter);
    }

    public Node addChild(char letter) {
        assertValidChar(letter);
        int index = ((int) letter) - ((int) 'A');
        if (children[index] == null) {
            children[index] = new Node();
        }
        return (children[index]);
    }

    public void setEndOfWord() {
        endOfWord = true;
    }

    public Node getChild(char letter) {
        assertValidChar(letter);
        int index = ((int) letter) - ((int) 'A');
        return children[index];
    }

    public boolean isChild(char letter) {
        assertValidChar(letter);
        int index = ((int) letter) - ((int) 'A');
        return children[index] != null;
    }

    public boolean isEndOfWord() {
        return endOfWord;
    }
}
