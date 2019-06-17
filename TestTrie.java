import org.junit.Test;
import ucb.junit.textui;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/** The suite of all JUnit tests for the Trie class.
 *  @author
 */
public class TestTrie {

    /** A dummy test to avoid complaint. */
    @Test
    public void placeholderTest() {

            Trie t = new Trie();
            t.insert("hello");
            t.insert("hey");
            t.insert("goodbye");
            assertTrue (t.find("hell", false));
            assertTrue(t.find("hello", true));
            assertTrue(t.find("good", false));
            assertFalse(t.find("bye", false));
            assertFalse(t.find("heyy", false));
            assertFalse(t.find("hell", true));
    }

    @Test
    public void insertExceptions(){
        boolean thrown1 = false;
        boolean thrown2 = false;
        Trie t = new Trie();

        try {
            t.insert("");
        } catch (IllegalArgumentException e) {
            thrown1 = true;
        }

        try {
            t.insert(null);
        } catch (IllegalArgumentException e) {
            thrown2 = true;
        }

        assertTrue(thrown1);
        assertTrue(thrown2);

    }

    /** Run the JUnit tests above. */
    public static void main(String[] ignored) {
        textui.runClasses(TestTrie.class);
    }
}
