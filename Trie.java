import java.util.*;

/**
 * Prefix-Trie. Supports linear time find() and insert().
 * Should support determining whether a word is a full word in the
 * Trie or a prefix.
 *
 * @author
 */
public class Trie {

    private TrieNode root;

    //    private String alphabet;
    private Map<Character, Integer> order;

    private boolean flag; // for making a custom or ordinary ordering of the Trie

    public Trie() {

        }

    public Trie(String permutation) {
//        alphabet = permutation;

        flag = true; // need to make a custom comparator

        this.order = new HashMap<>();
        for (int i = 0; i < permutation.length(); i++) {
            order.put(permutation.charAt(i), i);
        }
    }

    private int getOrder(char c) {
        Integer value = order.get(c);
        if (value == null) return -1; // safety
        return value;
    }


    public boolean find(String s, boolean isFullWord) {
        /* YOUR CODE HERE. */

        if (s == null || s.length() == 0) {
            throw new IllegalArgumentException("Null and empty string are not allowed in the trie");
        }

        if (root == null) { // empty trie!
            return false;
        }

        char[] arr = s.toCharArray();
        TrieNode curr = root;

        for (int i = 0; i < arr.length; i++) {
            char c = arr[i];

            if (curr.getChar(c) == null) {
                return false;
            } else {
                curr = curr.getChar(c);
            }
        }

        if (!isFullWord) { // check if prefix
            if (curr.getMap().size() != 0) { // something else is there!
                return true;
            }
            return false;

        } else { // check for full word
            return curr.getisWord();
        }
    }


    public void insert(String s) {
        /* YOUR CODE HERE. */

        if (s == null || s.length() == 0) {
            throw new IllegalArgumentException("Null and empty string are not allowed in the trie");
        }

        if (root == null) {

            root = new TrieNode(flag);
        }

        TrieNode curr = root;

        char[] arr = s.toCharArray();

        for (int i = 0; i < arr.length; i++) {

            if (curr.getChar(arr[i]) != null) {

                curr = curr.getChar(arr[i]); // walk down the trie

            } else { // put in the curr node's map and return next node in the tree

                String prevData = curr.getPrefix(); // save data from earlier node

                curr = curr.putChar(arr[i]); // move to the next node

                curr.setPrefix(prevData + arr[i]); //  append to next node prevData and current character

//                System.out.println(curr.prefix);
            }
        }

        curr.setWord(); // Set next TrieNode to true
//        System.out.println(curr.map.get('o'));
//        System.out.println(curr.prefix);
//        System.out.println(curr.isWord);
//        System.out.println(curr.map.size());
    }

    public void printWords() {

        printHelper(root, "");
    }

    public void printHelper(TrieNode node, String prefix) {
        if (node.getisWord()) {
            System.out.println(prefix);
//            return;
        }

        for (Character c : node.getMap().keySet()) {

            TrieNode n = node.getChar(c);

            prefix = n.getPrefix(); // just an assignment - not adding stuff at each level because I am implicitly storing the "building word" at each level

            printHelper(n, prefix);
        }
    }

    private class TrieNode {
        private SortedMap<Character, TrieNode> map;
        private boolean isWord;

        private String prefix;

        private TrieNode(boolean flag) {
            if (flag) {
                map = new TreeMap<>(new comp());
            } else {
                map = new TreeMap<>();
            }

            isWord = false;
            prefix = "";
        }

        /*
        Getters and Setters
         */

        public SortedMap<Character, TrieNode> getMap() {

            return map;
        }


        public boolean getisWord() {

            return isWord;
        }


        public void setWord() {

            isWord = true;
        }

        /**
         * @param c: the potential key in the Map
         * @return the TrieNode associated with the character
         */
        public TrieNode getChar(Character c) {

            return map.get(c);
        }

        /**
         * @param c: the character to insert in the map
         * @return the new TrieNode in the tree
         */
        public TrieNode putChar(Character c) {
            map.put(c, new TrieNode(flag)); // insert the key in current map and new TrieNode object
            return map.get(c);
        }

        public String getPrefix() {

            return prefix;
        }

        public void setPrefix(String s) {
            prefix = s;
        }

    }

    class comp implements Comparator<Character> {
        @Override
        public int compare(Character o1, Character o2) {

            return Integer.compare(getOrder(o1), getOrder(o2));
        }
    }


    public static void main(String[] args) {


        // Printing in natural ordering works

        Trie test = new Trie();
        test.insert("zebra");
        test.insert("dog");
        test.insert("abstract");
        test.insert("about");
        test.insert("able");

        test.printWords();

        System.out.println();

        // Printing based on alpha permutation works

        Trie test2 = new Trie("agdbecfhijklmnopqrsty");
        test2.insert("hello");
        test2.insert("goodbye");
        test2.insert("goodday");
        test2.insert("death");

        test2.printWords();

    }

}

