import java.util.Arrays;

/**
 * Note that every sorting algorithm takes in an argument k. The sorting 
 * algorithm should sort the array from index 0 to k. This argument could
 * be useful for some of your sorts.
 *
 * Class containing all the sorting algorithms from 61B to date.
 *
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 *
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 *
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 *
 * All implementations except Counting Sort adopted from Algorithms,
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /**
     * Java's Sorting Algorithm. Java uses Quicksort for ints.
     */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }
    }

    /** Insertion sorts the provided data. */
    public static class InsertionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // [5, 10, 1, 4, 3, 3, 11, 2]
            for (int i = 1; i < k; i += 1) {
             int j = i;
             while (j > 0 && array[j] < array[j-1]) {
                 int temp = array[j];
                 array[j] = array[j - 1];
                 array[j - 1] = temp;
                 j -= 1;
             }
           }
        }

        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            for (int i = 0; i < k; i += 1) {
                int index = i;
                int minNum = array[i];
                for (int j = i; j < k; j += 1) {
                    if (array[j] < minNum) {
                        minNum = array[j];
                        index = j;
                    }
                }
                int temp = array[i];
                array[i] = array[index];
                array[index] = temp;
            }
        }
        @Override
        public String toString() {
            return "Selection Sort";
        }
    }

    /** Your mergesort implementation. An iterative merge
      * method is easier to write than a recursive merge method.
      * Note: I'm only talking about the merge operation here,
      * not the entire algorithm, which is easier to do recursively.
      */
    public static class MergeSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            int[] copy = new int[k];
            System.arraycopy(array, 0, copy, 0, k);
            mergeSort(copy);
            System.arraycopy(copy, 0, array, 0, k);
        }

        public int[] mergeSort (int[] array) {
            if (array.length <= 1) {
                return array;
            }
            int[] firstHalf = new int[array.length/2];
            int[] secondHalf = new int[array.length - firstHalf.length];
            System.arraycopy(array, 0, firstHalf, 0, firstHalf.length);
            System.arraycopy(array, firstHalf.length, secondHalf, 0, secondHalf.length);
            mergeSort(firstHalf);
            mergeSort(secondHalf);
            mergeArrays(firstHalf, secondHalf, array);
            return array;
        }
        // may want to add additional methods
        public void mergeArrays(int[] arr1, int[] arr2, int[] mergedInto) {
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
        @Override
        public String toString() {
            return "Merge Sort";
        }
    }

    /**
     * Your Counting Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class CountingSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME: to be implemented
        }

        // may want to add additional methods

        @Override
        public String toString() {
            return "Counting Sort";
        }
    }

    /** Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Heap Sort";
        }
    }

    /** Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Quicksort";
        }
    }

    /* For radix sorts, treat the integers as strings of x-bit numbers.  For
     * example, if you take x to be 2, then the least significant digit of
     * 25 (= 11001 in binary) would be 1 (01), the next least would be 2 (10)
     * and the third least would be 1.  The rest would be 0.  You can even take
     * x to be 1 and sort one bit at a time.  It might be interesting to see
     * how the times compare for various values of x. */

    /**
     * LSD Sort implementation.
     */
    public static class LSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // [101, 10001101, 100, 101011000, 1001, 11111]

            int[] oldSorted = new int[k];
            System.arraycopy(a, 0, oldSorted, 0, k);
            int[] newSorted = new int[k];
            for (int i = 0; i <= 31; i += 1) {
                int counter = 0;
                for (int j = 0; j < k; j += 1) {
                    if ((oldSorted[j] >>> i) % 2 == 0) { // Change % 2 & shift to change how much sorting
                        newSorted[counter] = oldSorted[j];
                        counter += 1;
                    }
                }
                for (int j = 0; j < k; j += 1) {
                    if ((oldSorted[j] >>> i) % 2 == 1) { // Change % 2 & shift to chane how much sorting
                        newSorted[counter] = oldSorted[j];
                        counter += 1;
                    }
                }
                System.arraycopy(newSorted, 0, oldSorted, 0, k);
            }
            System.arraycopy(newSorted, 0, a, 0, k);
        }

        @Override
        public String toString() {
            return "LSD Sort";
        }
    }

    /**
     * MSD Sort implementation.
     */
    public static class MSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "MSD Sort";
        }
    }

    /** Exchange A[I] and A[J]. */
    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}
