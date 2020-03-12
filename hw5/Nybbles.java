/** Represents an array of integers each in the range -8..7.
 *  Such integers may be represented in 4 bits (called nybbles).
 *  @author Zachary Zhu
 */
public class Nybbles {

    /** Maximum positive value of a Nybble. */
    public static final int MAX_VALUE = 7;

    /** Return an array of size N. 
    * DON'T CHANGE THIS.*/
    public Nybbles(int N) {
        _data = new int[(N + 7) / 8];
        _n = N;
    }

    /** Return the size of THIS. */
    public int size() {
        return _n;
    }

    /** Return the Kth integer in THIS array, numbering from 0.
     *  Assumes 0 <= K < N. */
    public int get(int k) {
        if (k < 0 || k >= _n) {
            throw new IndexOutOfBoundsException();
        } else {
            int toGet = k / 8;
            int shiftBy = 4 * (k % 8);
            int copy = _data[toGet];
            int digits = copy >>> shiftBy;
            int fifteenMask = 15;
            int num = digits & fifteenMask;
            if (num >= 8) {
                return num - 16;
            } else {
                return num;
            }
        }
    }

    /** Set the Kth integer in THIS array to VAL.  Assumes
     *  0 <= K < N and -8 <= VAL < 8. */
    public void set(int k, int val) {
        if (k < 0 || k >= _n) {
            throw new IndexOutOfBoundsException();
        } else if (val < (-MAX_VALUE - 1) || val > MAX_VALUE) {
            throw new IllegalArgumentException();
        } else {
                int toSet = k / 8;
                int fifteenMaskOne = 15;
                fifteenMaskOne = fifteenMaskOne << (k * 4);
                fifteenMaskOne = ~fifteenMaskOne;
                int andOriginal = fifteenMaskOne & _data[toSet];
                int fifteenMaskTwo = 15;
                int andVal = fifteenMaskTwo & val;
                andVal = andVal << (k * 4);
                _data[toSet] = andOriginal | andVal;
        }
    }

    /** DON'T CHANGE OR ADD TO THESE.*/
    /** Size of current array (in nybbles). */
    private int _n;
    /** The array data, packed 8 nybbles to an int. */
    private int[] _data;
}
