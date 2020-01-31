import static org.junit.Assert.*;
import org.junit.Test;

public class MultiArrTest {

    @Test
    public void testMaxValue() {
        assertEquals(10, MultiArr.maxValue(new int[][]{{1,2,3}, {4,5,6}, {10, 9, 7}}));
        assertEquals(10, MultiArr.maxValue(new int[][]{{10, 9, 7}}));
        assertEquals(20, MultiArr.maxValue(new int[][]{{10}, {9}, {7}, {20}}));
    }

    @Test
    public void testAllRowSums() {
        assertArrayEquals(new int[]{6, 15, 26}, MultiArr.allRowSums(new int[][]{{1,2,3}, {4,5,6}, {10, 9, 7}}));
        assertArrayEquals(new int[]{26}, MultiArr.allRowSums(new int[][]{{10, 9, 7}}));
        assertArrayEquals(new int[]{10, 9, 7, 20}, MultiArr.allRowSums(new int[][]{{10}, {9}, {7}, {20}}));
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(MultiArrTest.class));
    }
}
