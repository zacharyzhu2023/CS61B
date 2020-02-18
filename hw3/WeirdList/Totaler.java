public class Totaler implements IntUnaryFunction {

    private int total;
    /** Constructor that initializes the number that will be added to **/
    public Totaler(int initialTotal) {
        total = initialTotal;
    }

    /** Implementation of the apply method for the sum method **/
    @Override
    public int apply (int x) {
        total += x;
        return total;
    }

    /** Method to get the final sum **/
    public int getTotal() {
        return total;
    }



}
