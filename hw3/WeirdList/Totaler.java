/** Class that totals.
 * @author Zachary Zhu
 */

public class Totaler implements IntUnaryFunction {

    /** instance variable total.
     *  **/
    private int total;
    /** Constructor that initializes the number that will be added to.
     * @param initialTotal starting point
     *  **/
    public Totaler(int initialTotal) {
        total = initialTotal;
    }

    /** Implementation of the apply method for the sum method.
     *  **/
    @Override
    public int apply(int x) {
        total += x;
        return total;
    }

    /** Method to get the final sum.
     * @return returns the total
     *  **/
    public int getTotal() {
        return total;
    }



}
