import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.TreeSet;

/**
 * AlphabetSort takes input from stdin and prints to stdout.
 * The first line of input is the alphabet permutation.
 * The the remaining lines are the words to be sorted.
 * <p>
 * The output should be the sorted words, each on its own line,
 * printed to std out.
 */
public class AlphabetSort {


    public static Set<Character> dictionary(String s) {
        Set<Character> valid = new TreeSet<>();

        for (int i = 0; i < s.length(); i++) {
//            System.out.println("puke");

            if (valid.contains(s.charAt(i))) {
//                System.out.println(s.charAt(i));

                throw new IllegalArgumentException("A letter appears multiple times in the alphabet.");
//                System.out.println(s.charAt(i));
            }

            valid.add(s.charAt(i));
        }
        return valid;
    }

    /**
     * Reads input from standard input and prints out the input words in
     * alphabetical order.
     *
     * @param args ignored
     */


    public static void main(String[] args) {
        /* YOUR CODE HERE. */

        // for the valid alphabets
        Set<Character> letters = new TreeSet<>();

        // Initialize a Trie object
        Trie obj = new Trie();

        // Reading from file
        BufferedReader in = null;

        try {
            in = new BufferedReader(new InputStreamReader(System.in));

//            System.out.println("1");
//            System.out.println("2");
//            System.out.println(in.readLine());

            String line;

            boolean alphabet = true;
            boolean firstWord = true;

            line = in.readLine();

            if (line == null || line.length() == 0){
                throw new IllegalArgumentException("No words or alphabet are given."); // works on an empty file)
            }
            letters = dictionary(line);

            obj = new Trie(line);

            line = in.readLine();

            if (line == null || line.length() == 0){
                throw new IllegalArgumentException("No words are given.");
            }


            boolean notInAlphabet = false;
            for (int i = 0; i < line.length(); i++) {
                if (!letters.contains(line.charAt(i))) {
                    notInAlphabet = true;
                }
            }

            if (!notInAlphabet) {
                obj.insert(line);
            }
            line = in.readLine();
            while (line != null) {

                notInAlphabet = false;
                for (int i = 0; i < line.length(); i++) {
                    if (!letters.contains(line.charAt(i))) {
                        notInAlphabet = true;
                    }
                }

                if (!notInAlphabet) {
                    obj.insert(line);
                }


                line = in.readLine(); // key
            }


            obj.printWords();

        } catch (IOException e) {
            System.out.println("hello");
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
