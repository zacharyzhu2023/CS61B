/** A class that can add.
 * @author Zachary Zhu
 *  */
public class Adder implements IntUnaryFunction {

    /** Instance variable for what will get added.
     *  **/
    private int constant;

    /** Constructor that determines constant number
     * that will be added to other numbers.
     * @param n int to be added
     *  **/
    public Adder (int n) {
        constant = n;
    }

    /** Implementation of the apply method to be used
     * in add method.
     * @param x apply method with constant and value
     * **/
    @Override
    public int apply (int x) {
        return x + constant;
    }
}
