import org.junit.Test;
import static org.junit.Assert.*;

import ucb.junit.textui;

/** Tests for hw0.
 *  @author Zachary Zhu
 */
public class Tester {

    /* Feel free to add your own tests.  For now, you can just follow
     * the pattern you see here.  We'll look into the details of JUnit
     * testing later.
     *
     * To actually run the tests, just use
     *      java Tester
     * (after first compiling your files).
     *
     * DON'T put your HW0 solutions here!  Put them in a separate
     * class and figure out how to call them from here.  You'll have
     * to modify the calls to max, threeSum, and threeSumDistinct to
     * get them to work, but it's all good practice! */

    @Test
    public void maxTest() {

        hw0 maxTester = new hw0();
        assertEquals(14, maxTester.max(new int[] { 0, -5, 2, 14, 10 }));
        assertEquals(0, maxTester.max(new int[] {0}));
        assertEquals(12, maxTester.max(new int[] {2, 5, 10, -11, 12}));

    }

    @Test
    public void threeSumTest() {

        hw0 threeSumTestTester = new hw0();
        assertTrue(threeSumTestTester.threeSum(new int[] { -6, 3, 10, 200 }));
        assertTrue(threeSumTestTester.threeSum(new int[] { -10, 10, 5 }));
        assertFalse(threeSumTestTester.threeSum(new int[] { -5, -6, -7, -8 }));
        assertTrue(threeSumTestTester.threeSum(new int[] { 0, 6, 1 }));

    }

    @Test
    public void threeSumDistinctTest() {

        hw0 threeSumDistinctTestTester = new hw0();
        assertFalse(threeSumDistinctTestTester.threeSumDistinct(new int[] { -6, 3, 10, 200 }));
        assertTrue(threeSumDistinctTestTester.threeSumDistinct(new int[] { -5, 5, 0}));
        assertFalse(threeSumDistinctTestTester.threeSumDistinct(new int[] { 0, 6, 1, -5 }));
        assertTrue(threeSumDistinctTestTester.threeSumDistinct(new int[] { -10, -5, 14, 13, 100, 15 }));

    }

    public static void main(String[] unused) {
        textui.runClasses(Tester.class);
    }

}
