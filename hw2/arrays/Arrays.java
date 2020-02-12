package arrays;

/* NOTE: The file Arrays/Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

/** Array utilities.
 *  @author
 */
class Arrays {

    /* C1. */
    /** Returns a new array consisting of the elements of A followed by the
     *  the elements of B. */
    static int[] catenate(int[] A, int[] B) {
        if (A.length == 0) {
            return B;
        } else if (B.length == 0) {
            return A;
        } else {
            int[] finalArr = new int[A.length + B.length];
            for (int i = 0; i < A.length; i += 1) {
                finalArr[i] = A[i];
            }
            for (int j = 0; j < B.length; j += 1) {
                finalArr[A.length + j] = B[j];
            }
            return finalArr;
        }
    }

    /* C2. */
    /** Returns the array formed by removing LEN items from A,
     *  beginning with item #START. */
    static int[] remove(int[] A, int start, int len) {
        int[] finalArr = new int[A.length - len];
        System.arraycopy(A, 0, finalArr, 0, start);
        System.arraycopy(A, start + len, finalArr, start, A.length - start - len);
        return finalArr;
    }

    /* C3. */
    /** Returns the array of arrays formed by breaking up A into
     *  maximal ascending lists, without reordering.
     *  For example, if A is {1, 3, 7, 5, 4, 6, 9, 10}, then
     *  returns the three-element array
     *  {{1, 3, 7}, {5}, {4, 6, 9, 10}}. */
    static int[][] naturalRuns(int[] A) {
        if (A.length == 0) {
            return new int[][]{};
        } else if (A.length == 1) {
            return new int[][]{A};
        }
        int length = 0;
        for (int i = 0; i < A.length - 1; i++) {
            if (A[i + 1] <= A[i]) {
                length += 1;
            }
        }
        int[][] finalArr = new int[length + 1][];
        String s = "";
        int counter = 0;
        for (int i = 0; i < A.length - 1; i++) {
            s = s + A[i] + " ";
            if (i + 2 == A.length) {
                if (A[i + 1] <= A[i]) {
                    String[] splitString = s.split(" ");
                    int[] numArr = new int[splitString.length];
                    for (int j = 0; j < splitString.length; j += 1) {
                        numArr[j] = Integer.parseInt(splitString[j]);
                    }
                    finalArr[counter] = numArr;
                    finalArr[counter + 1] = new int[] {A[i + 1]};
                } else {
                    s = s + A[i + 1];
                    String[] splitString = s.split(" ");
                    int[] numArr = new int[splitString.length];
                    for (int j = 0; j < splitString.length; j += 1) {
                        numArr[j] = Integer.parseInt(splitString[j]);
                    }
                    finalArr[counter] = numArr;
                }
                return finalArr;

            }
            if (A[i + 1] <= A[i]) {
                String[] splitString = s.split(" ");
                int[] numArr = new int[splitString.length];
                for (int j = 0; j < splitString.length; j += 1) {
                    numArr[j] = Integer.parseInt(splitString[j]);
                }
                finalArr[counter] = numArr;
                counter += 1;
                s = "";

            }
        }
        System.out.println("PRINTED");
        return finalArr;
    }
}
