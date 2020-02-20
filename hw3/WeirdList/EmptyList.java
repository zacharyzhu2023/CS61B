/** Empty version of the WeirdList.
 * @author Zachary Zhu
 */

public class EmptyList extends WeirdList {

    /** Constructor for an EmptyList.
     *  **/
    public EmptyList() {
        super(0, null);
    }

    /** Length of an EmptyList.
     *  **/
    @Override
    public int length() {
        return 0;
    }

    /** "Representation" for an EmptyList.
     *  **/
    @Override
    public String toString() {
        return "";
    }

    /** Map function for an EmptyList.
     * @param func function taken in
     *  **/
    @Override
    public WeirdList map(IntUnaryFunction func) {
        return new EmptyList();
    }

}
