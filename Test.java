import java.util.*;


/**
 * Created by dhruvsawhney on 8/1/17.
 */
public class Test {


    public static void main(String[] args) {


        // check if a word exists -- get reference to the map

        Map<Character,Integer> alphabets;
        Test.PermutationComparator perm = new Test.PermutationComparator("agdbecfhijklmnopqrsty");
        alphabets = perm.getMap();


        String s1 = "hello";

        for (int i = 0 ; i < s1.length(); i++){
            char c = s1.charAt(i);

            if (!alphabets.containsKey(c)){
                System.out.println("False");
            }
        }

            // add to a TreeSet based on the custom comparator
            SortedMap<String,String> map = new TreeMap<>(new PermutationComparator("agdbecfhijklmnopqrsty"));
            map.put("hello","fourth");
            map.put("goodbye","second");
            map.put("goodday","first");
            map.put("death","third");

            System.out.println(map);


        ArrayList<String> result = new ArrayList<>();

        try {
            result.get(0);
        }catch (IndexOutOfBoundsException e){
            System.out.println(5);
        }


    }

    /**
     * Referenced from Stack Overflow
     */
    static class PermutationComparator implements Comparator<String> {
        private final Map<Character, Integer> order;

        public PermutationComparator(String permutation) {
            this.order = new HashMap<Character, Integer>();
            for (int i = 0; i < permutation.length(); i++) {
                order.put(permutation.charAt(i), i);
            }
        }

        private int getOrder(char c) {
            Integer value = order.get(c);
            if (value == null) return -1; // safety
            return value;
        }

        @Override
        public int compare(String o1, String o2) {
            for (int i = 0; i < Math.min(o1.length(), o2.length()); i++) {
                int compare = Integer.compare(getOrder(o1.charAt(i)), getOrder(o2.charAt(i)));
                if (compare != 0) return compare;
            }
            return Integer.compare(o1.length(), o2.length()); // safety
        }

        public Map getMap(){
            return order;
        }

    }

}
