import java.util.Arrays;

/** HW #7, Two-sum problem.
 * @author
 */
public class Sum {

    /** Returns true iff A[i]+B[j] = M for some i and j. */
    public static boolean sumsTo(int[] A, int[] B, int m) {
        Arrays.sort(A);
        Arrays.sort(B);
        int[] result = new int[A.length + B.length];
        mergeArrays(A, B, result);
        int frontPointer = 0, backPointer = result.length - 1;
        while (frontPointer < backPointer) {
            int total = result[frontPointer] + result[backPointer];
            if (total == m) return true;
            else if (total < m) frontPointer += 1;
            else backPointer -= 1;
        }
        return false;
    }

    public static void mergeArrays(int[] arr1, int[] arr2, int[] mergedInto) {
        int counter = 0, counter1 = 0, counter2 = 0;
        while (counter < arr1.length + arr2.length) {
            if (counter1 == arr1.length || (counter2 < arr2.length && arr1[counter1] >= arr2[counter2])) {
                mergedInto[counter] = arr2[counter2];
                counter2 += 1;
            } else {
                mergedInto[counter] = arr1[counter1];
                counter1 += 1;
            }
            counter += 1;
        }
    }

}
