import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author Zachary Zhu
 */
public class BSTStringSetTest  {
    // FIXME: Add your own tests for your BST StringSet

    @Test
    public void testPut() {
        BSTStringSet t = new BSTStringSet();
        t.put("hello");
        t.put("good");
        t.put("salutations");
        t.put("zz");
        t.put("igloo");
        t.put("iii");
        t.put("iiii");
        assertTrue(t.getBSTLabel().equals("hello"));
    }

    @Test
    public void testContains() {
        BSTStringSet t = new BSTStringSet();
        t.put("hello");
        t.put("good");
        t.put("salutations");
        t.put("zz");
        t.put("igloo");
        t.put("iii");
        t.put("iiii");
        assertTrue(t.contains("hello"));
        assertTrue(t.contains("zz"));
        assertFalse(t.contains("iiiii"));
        assertFalse(t.contains("tester"));

    }

    @Test
    public void testasList() {
        BSTStringSet t = new BSTStringSet();
        t.put("hello");
        t.put("good");
        t.put("salutations");
        t.put("zz");
        t.put("igloo");
        t.put("iii");
        t.put("iiii");
        t.asList();
    }

    @Test
    public void testRangeIterator() {
        BSTStringSet t = new BSTStringSet();
        t.put("hello");
        t.put("good");
        t.put("salutations");
        t.put("zz");
        t.put("igloo");
        t.put("iii");
        t.put("iiii");
        t.printRange();
    }

}
