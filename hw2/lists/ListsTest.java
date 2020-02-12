package lists;

import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *
 *  @author FIXME
 */

public class ListsTest {

    @Test
    public void naturalRunsTest() {
        int[] arr1 = new int[] {1, 3, 7, 5, 4, 6, 9, 10, 10, 11};
        int [][] arrr1 = new int[][] {{1, 3, 7}, {5}, {4, 6, 9, 10, 10, 11}};
        IntListList ill1 = IntListList.list(arrr1);
        IntList il1 = IntList.list(arr1);
        ill1.equals(Lists.naturalRuns(il1));

        int[] arr2 = new int[] {1, -1, -5};
        int [][] arrr2 = new int[][] {{1}, {-1}, {-5}};
        IntListList ill2 = IntListList.list(arrr2);
        IntList il2 = IntList.list(arr2);
        ill2.equals(Lists.naturalRuns(il2));

        int[] arr3 = new int[] {3, 4};
        int [][] arrr3 = new int[][] {{3, 4}};
        IntListList ill3 = IntListList.list(arrr3);
        IntList il3 = IntList.list(arr3);
        ill3.equals(Lists.naturalRuns(il3));


    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ListsTest.class));
    }
}
