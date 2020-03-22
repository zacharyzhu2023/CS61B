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
        tester.put("six");
        tester.put("seven");
        tester.put("eight");
        tester.put("nine");
        tester.put("ten");
        tester.put("eleven");
        tester.put("twelve");
        tester.put("thirteen");
        tester.put("fourteen");
        tester.put("fifteen");
        tester.put("sixteen");
        System.out.println(tester.asList());
        assertTrue(tester.contains("eight"));
        assertFalse(tester.contains("once"));
    }
}
