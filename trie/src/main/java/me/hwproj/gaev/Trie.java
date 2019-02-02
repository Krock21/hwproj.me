package me.hwproj.gaev;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * Trie can save and remove strings in itself, and can answer questions:
 * "does Trie contains such string",
 * "how many strings in Trie",
 * "how many strings in Trie starts with such prefix".
 * Also Trie implements interface Serializable
 */
public class Trie implements Serializable {

    private static class Node implements Serializable {
        private boolean containsStringHere;
        private int countOfStringsFurther;
        private final HashMap<Character, Node> nextNodes = new HashMap<>();// Hashmap instead of Map for speed guarantee
        private Node parentNode;

        Node() {
        }

        Node(Node parentNode) {
            this.parentNode = parentNode;
        }

        private boolean isContainsStringHere() {
            return containsStringHere;
        }

        private void setContainsStringHere(boolean containsStringHere) {
            this.containsStringHere = containsStringHere;
        }

        private int getCountOfStringsFurther() {
            return countOfStringsFurther;
        }

        private void setCountOfStringsFurther(int countOfStringsFurther) {
            this.countOfStringsFurther = countOfStringsFurther;
        }

        private Node getParentNode() {
            return parentNode;
        }

        private void setParentNode(Node parentNode) {
            this.parentNode = parentNode;
        }

        /**
         * returns Node, which is obtained by passage on Character c, or null
         *
         * @param c Character to move
         */
        private Node move(Character c) {
            return nextNodes.getOrDefault(c, null);
        }

        /**
         * returns Node, which is obtained by passage on Character c. Creates new node and returns it, if it was not.
         *
         * @param c Character to move
         */
        private @NotNull Node moveOrAdd(@NotNull Character c) {
            if (!nextNodes.containsKey(c)) {
                nextNodes.put(c, new Node(this));
            }
            return nextNodes.get(c);
        }

        @Override
        public void serialize(@NotNull OutputStream out) throws IOException {
            try (var objectOutput = new ObjectOutputStream(out)) {
                objectOutput.writeBoolean(containsStringHere);
                objectOutput.writeInt(countOfStringsFurther);
                objectOutput.writeInt(nextNodes.size());
                for (var current : nextNodes.entrySet()) {
                    objectOutput.writeChar(current.getKey());
                    objectOutput.flush();
                    current.getValue().serialize(out);
                }
                objectOutput.flush();
            }
        }

        @Override
        public void deserialize(@NotNull InputStream in) throws IOException {
            try (var objectInput = new ObjectInputStream(in)) {
                containsStringHere = objectInput.readBoolean();
                countOfStringsFurther = objectInput.readInt();
                var nextNodesSize = objectInput.readInt();
                for (int i = 0; i < nextNodesSize; i++) {
                    var key = objectInput.readChar();
                    var value = new Node();
                    value.deserialize(in);
                    value.parentNode = this;
                    nextNodes.put(key, value);
                }
            }
            parentNode = null;
        }
    }

    private final @NotNull Node root;

    private @Nullable Node moveOrNull(@NotNull String element) {
        Node currentNode = root;
        for (var c : element.toCharArray()) {
            currentNode = currentNode.move(c);
            if (currentNode == null) {
                break;
            }
        }
        return currentNode;
    }

    public Trie() {
        root = new Node();
        root.setParentNode(root);
    }

    /**
     * adds element to a trie
     *
     * @return true, if there was not such String in trie, false otherwise
     */
    public boolean add(@NotNull String element) {// (возвращает true, если такой строки ещё не было, работает за O(|element|))
        @NotNull Node currentNode = root;
        for (var c : element.toCharArray()) {
            currentNode = currentNode.moveOrAdd(c);
        }
        boolean answer = false;
        if (!currentNode.isContainsStringHere()) {
            currentNode.setContainsStringHere(true);
            answer = true;
            while (currentNode.parentNode != currentNode) {
                currentNode.setCountOfStringsFurther(currentNode.getCountOfStringsFurther() + 1);
                currentNode = currentNode.getParentNode();
            }
            currentNode.setCountOfStringsFurther(currentNode.getCountOfStringsFurther() + 1);
        }
        return answer;
    }

    /**
     * checks trie for containing the element
     *
     * @return true, if trie contains element, false otherwise
     */
    public boolean contains(@NotNull String element) { //(работает за O(|element|))
        Node node = moveOrNull(element);
        boolean answer = false;
        if (node != null) {
            answer = node.isContainsStringHere();
        }
        return answer;
    }

    /**
     * removes element from the trie
     *
     * @return true, is the same element was in trie before, false otherwise
     */
    public boolean remove(@NotNull String element) { // (возвращает true, если элемент реально был в дереве, работает за O(|element|))
        Node node = moveOrNull(element);
        boolean answer;
        if (node == null) {
            answer = false;
        } else if (node.isContainsStringHere()) {
            node.setContainsStringHere(false);
            while (node.parentNode != node) {
                node.setCountOfStringsFurther(node.getCountOfStringsFurther() - 1);
                node = node.getParentNode();
            }
            node.setCountOfStringsFurther(node.getCountOfStringsFurther() - 1);
            answer = true;
        } else {
            answer = false;
        }
        return answer;
    }

    /**
     * returns count of strings, added to the trie
     */
    public int size() { // (работает за O(1))
        return howManyStartsWithPrefix("");
    }

    /**
     * returns count of strings in trie, that starts from prefix
     */
    public int howManyStartsWithPrefix(@NotNull String prefix) { // (работает за O(|prefix|))
        Node node = moveOrNull(prefix);
        int answer = 0;
        if (node != null) {
            answer = node.getCountOfStringsFurther();
        }
        return answer;
    }

    /**
     * serializes the trie to out
     */
    @Override
    public void serialize(@NotNull OutputStream out) throws IOException {
        root.serialize(out);
    }

    /**
     * deserializes the trie from in
     */
    @Override
    public void deserialize(@NotNull InputStream in) throws IOException {
        root.deserialize(in);
        root.setParentNode(root);
    }
}
