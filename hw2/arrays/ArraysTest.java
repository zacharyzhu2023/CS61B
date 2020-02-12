package arrays;

import org.junit.Test;
import static org.junit.Assert.*;

/** hw2
 *  @author Zachary Zhu
 */

public class ArraysTest {
    /** FIXME
     */

    @Test
    public void catenateTest() {
        assertArrayEquals(new int[] {1, 2, 3, 4, 5, 6},
                Arrays.catenate(new int[] {1, 2, 3}, new int[] {4, 5, 6}));
        assertArrayEquals(new int[] {2, 2, 5, 10, 11},
                Arrays.catenate(new int[] {2, 2}, new int[] {5, 10, 11}));
        assertArrayEquals(new int[] {1, 2, 3},
                Arrays.catenate(new int []{1, 2, 3}, new int[] {}));
        assertArrayEquals(new int[] {},
                Arrays.catenate(new int []{}, new int[] {}));
    }

    @Test
    public void removeTest() {
        assertArrayEquals(new int[] {1, 5, 13},
                Arrays.remove(new int[] {1, 5, 6, 10, 13}, 2, 2));
        assertArrayEquals(new int[] {10, 13},
                Arrays.remove(new int[] {1, 5, 6, 10, 13}, 0, 3));
        assertArrayEquals(new int[] {1, 5, 6, 10, 13},
                Arrays.remove(new int[] {1, 5, 6, 10, 13}, 3, 0));
        assertArrayEquals(new int[] {1, 5, 6, 10},
                Arrays.remove(new int[] {1, 5, 6, 10, 13}, 4, 1));
    }

    @Test
    public void naturalRunsTest() {
        assertArrayEquals(new int[][] {{1, 3, 7}, {2, 4, 6}, {5}, {5}},
                Arrays.naturalRuns(new int[] {1, 3, 7, 2, 4, 6, 5, 5}));
        assertArrayEquals(new int[][] {{7}, {5}, {4}, {3, 4, 8, 10}},
                Arrays.naturalRuns(new int[] {7, 5, 4, 3, 4, 8, 10}));
        assertArrayEquals(new int[][] {{1, 2}},
                Arrays.naturalRuns(new int[] {1, 2}));
        assertArrayEquals(new int[][] {{5}, {5}, {1}},
                Arrays.naturalRuns(new int[] {5, 5, 1}));
        assertArrayEquals(new int[][] {{5}, {5}},
                Arrays.naturalRuns(new int[] {5, 5}));
        assertArrayEquals(new int[][] {{5}, {5, 10}, {1}},
                Arrays.naturalRuns(new int[] {5, 5, 10, 1}));
        assertArrayEquals(new int[][] {},
                Arrays.naturalRuns(new int[] {}));
    }


    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
