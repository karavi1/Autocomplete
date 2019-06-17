import java.util.*;

/**
 * Prefix-Trie. Supports linear time find() and insert().
 * Should support determining whether a word is a full word in the
 * Trie or a prefix.
 *
 * @author
 */
public class AugTrie {

    private TrieNode root;

    // access to the two arrays from the Autocomplete class
    String[] words;
    double[] values;

    // create a HashSet for words to be inserted in the trie
    Set<String> set;

    public AugTrie() {
    }

    public AugTrie(String[] w, double[] val) {
        words = w;
        values = val;

        set = new TreeSet<>();
    }

    public String[] getWords() {
        return words;
    }

    public double[] getValues() {
        return values;
    }

    // Debugger1
    public TrieNode getNode(String s) {
        TrieNode curr = root;

        for (int i = 0; i < s.length(); i++) {
            curr = curr.getChar(s.charAt(i));
        }
        return curr;
    }


    public void printLeaves() {
        root.getLeaves(root);
    }

    public boolean find(String s, boolean isFullWord) {

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

    /**
     * Builds the trie based on the array
     */
    public void buildTrie() {
        String[] wd = getWords();
        double[] val = getValues();

        for (int i = 0; i < wd.length; i++) { // for every word and value: put it in the trie
            insert(wd[i], val[i]);
        }
    }

    // Modified to set the value and max value at the end node
    public void insert(String s, double val) {
        /* YOUR CODE HERE. */
        // Error cases
        if (val < 0){
            throw new IllegalArgumentException("There are negative weights");
        }

        if (s == null || s.length() == 0) {
            throw new IllegalArgumentException("Null and empty string are not allowed in the trie");
        }

        if (set.contains(s)){
            throw new IllegalArgumentException("There are duplicate input terms");
        }

        if (root == null) {

            root = new TrieNode();
        }

        TrieNode curr = root;

        char[] arr = s.toCharArray();

        for (int i = 0; i < arr.length; i++) {

            if (curr.getChar(arr[i]) != null) {

                curr = curr.getChar(arr[i]); // walk down the trie

            } else { // put in the curr node's map and return next node in the tree

                String prevData = curr.getPrefix(); // save data from earlier node
                curr = curr.putChar(arr[i]);
                curr.setPrefix(prevData + arr[i]);

            }
        }

        curr.setWord();
        set.add(s);
        curr.setVal(val);
        curr.setMax(val);

    }

    public void printWords() {

        printHelper(root, "");
    }

    public void printHelper(TrieNode node, String prefix) {
        if (node.getisWord()) {
            System.out.println(prefix + "   " + node.val); // at the nodes that are words we have the value for that word and the max = value
        }

        for (Character c : node.getMap().keySet()) {

            TrieNode n = node.getChar(c);

            prefix = n.getPrefix(); // just an assignment - not adding stuff at each level because I am implicitly storing the "building word" at each level

            printHelper(n, prefix);
        }
    }


    public void buildMax() {

        maxHelper(root);

    }

    // builds the correct maximum value for each node from the bottom up
    private Double maxHelper(TrieNode node) {
        if (node.getMap().size() == 0) { // at this point there is a max value for each "leaf"
            return node.getMax();
        } else {

            for (Character c : node.getMap().keySet()) {
                TrieNode n = node.getChar(c);

                Double newMax = maxHelper(n);

                if (newMax > node.getMax()) {
                    node.setMax(newMax);
                }
            }
            return node.getMax();
        }
    }


    public ArrayList<String> autoComplete(String s, int k) { //ArrayList<String>

        PriorityQueue<TrieNode> pqVal = new PriorityQueue<>(new compVal()); // PQ to add Nodes based on their "Val" instance in a min-heap

        TrieNode curr = getNode(s); // find the specific node

        PriorityQueue<TrieNode> pq = new PriorityQueue<>(Collections.reverseOrder()); // The PQ based on the MAX value of each node!: NAVIGATE THE SEARCH SPACE

        pq.add(curr); // start the search space

        autoHelper(pqVal, pq, k); // pass in pqVal to method

        ArrayList<String> result = new ArrayList<>();

        while (!pqVal.isEmpty()) {
            result.add(0, pqVal.poll().getPrefix());
        }
        return result;

    }

    /**
     *
     * @param bestAnswer: A PQ based on the instance var VALUE
     * @param pq: NAVIGATE THE SEARCH SPACE BASED ON MAX
     * @param k

     */
    public void autoHelper(PriorityQueue<TrieNode> bestAnswer, PriorityQueue<TrieNode> pq, int k) {

        if (pq.peek() == null) { // nothing left in the priority queue
            return;
        }
        TrieNode node = pq.poll(); // get the top-element from the pq

        if (bestAnswer.size() < k) {
            if (node.getisWord()) {
                bestAnswer.add(node); // add to back
            }

        } else {
            if (node.getMax() <= bestAnswer.peek().getVal()) {
                return;
            }

            if (node.getisWord()) {
                if (node.getVal() > bestAnswer.peek().getVal()) {
                    bestAnswer.poll(); // remove the smallest item, so after adding the new TrieNode, count is still k
                    bestAnswer.add(node); // add to back
                }
            }

        }

        for (Character c : node.getMap().keySet()) {
            TrieNode n = node.getChar(c);
            pq.add(n); // add all the nodes to the pq based on the max
        }

        autoHelper(bestAnswer, pq, k);
    }



    public class TrieNode implements Comparable<TrieNode> {
        private Map<Character, TrieNode> map;
        private boolean isWord;
        private String prefix;
        private Double val;
        private Double max;


        private TrieNode() {

            map = new TreeMap<>();


            isWord = false;
            prefix = "";

            // set the val and max to 0.0
            val = 0.0;
            max = 0.0;
        }

        /*
        Getters and Setters
         */

        public Map<Character, TrieNode> getMap() {

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
            map.put(c, new TrieNode()); // insert the key in current map and new TrieNode object
            return map.get(c);
        }

        public String getPrefix() {

            return prefix;
        }

        public void setPrefix(String s) {
            prefix = s;
        }


        public void setVal(double v) {
            val = v;
        }

        public void setMax(double m) {
            max = m;
        }

        public Double getMax() {
            return max;
        }

        public Double getVal() {
            return val;
        }

        // get all the leaves
        public void getLeaves(TrieNode root) {

            if (root.getMap().size() == 0) {
                System.out.println(root.getPrefix());
            } else {
                for (Character c : root.getMap().keySet()) {
                    TrieNode node = root.getChar(c);

                    getLeaves(node);
                }
            }
        }


        @Override
        public int compareTo(TrieNode o) {
            if (getMax() < o.getMax()){
                return -1;
            }else if (getMax() > o.getMax()){
                return 1;
            }else {
                return 0;
            }

        }

    }

    class compVal implements Comparator<TrieNode>{

        @Override
        public int compare(TrieNode o1, TrieNode o2) { // descending order
            if (o1.getVal() < o2.getVal()){
                return -1;
            } else if (o1.getVal() > o2.getVal() ){
                return 1;
            } else {
                return 0;
            }
        }
    }

}

