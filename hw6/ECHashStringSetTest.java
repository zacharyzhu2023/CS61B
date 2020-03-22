import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author
 */
public class ECHashStringSetTest  {
    // FIXME: Add your own tests for your ECHashStringSetTest

    @Test
    public void testECStringSet() {
        ECHashStringSet tester = new ECHashStringSet();
        tester.put("one");
        tester.put("two");
        tester.put("three");
        tester.put("four");
        tester.put("five");
        tester.put("again");
        tester.put("another");
        System.out.println(tester.asList());
        assertTrue(tester.contains("another"));
        assertFalse(tester.contains("once"));
    }
}
