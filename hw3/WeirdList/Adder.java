public class Adder implements IntUnaryFunction {

    /** Instance variable for what will get added **/
    private int constant;

    /** Constructor that determines constant number that will be added to other numbers **/
    public Adder(int n) {
        constant = n;
    }

    /** Implementation of the apply method to be used in add method**/
    @Override
    public int apply (int x) {
        return x + constant;
    }
}
