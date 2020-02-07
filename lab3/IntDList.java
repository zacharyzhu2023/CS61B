/**
 * Scheme-like pairs that can be used to form a list of integers.
 *
 * @author P. N. Hilfinger; updated by Vivant Sakore (1/29/2020)
 */
public class IntDList {

    /**
     * First and last nodes of list.
     */
    protected DNode _front, _back;

    /**
     * An empty list.
     */
    public IntDList() {
        _front = _back = null;
    }

    /**
     * @param values the ints to be placed in the IntDList.
     */
    public IntDList(Integer... values) {
        _front = _back = null;
        for (int val : values) {
            insertBack(val);
        }
    }

    /**
     * @return The first value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getFront() {
        return _front._val;
    }

    /**
     * @return The last value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getBack() {
        return _back._val;
    }

    /**
     * @return The number of elements in this list.
     */
    public int size() {
        int size = 0;
        DNode temp = _front;
        while (temp != null){
            size += 1;
            temp = temp._next;
        }
        return size;
    }

    /**
     * @param i index of element to return,
     *          where i = 0 returns the first element,
     *          i = 1 returns the second element,
     *          i = -1 returns the last element,
     *          i = -2 returns the second to last element, and so on.
     *          You can assume i will always be a valid index, i.e 0 <= i < size for positive indices
     *          and -size <= i <= -1 for negative indices.
     * @return The integer value at index i
     */
    public int get(int i) {
        if (i >= 0){
            DNode temp = this._front;
            while (i != 0){
                temp = temp._next;
                i -= 1;
            }
            return temp._val;
        }
        else{
            DNode temp = _back;
            while (i != -1){
                temp = temp._prev;
                i += 1;
            }
            return temp._val;
        }

    }

    /**
     * @param d value to be inserted in the front
     */
    public void insertFront(int d) {
        // FIXME: Implement this method
        if (_front == null){
            _front = _back = new DNode(d);
        }
        else{
            DNode temp = new DNode(d);
            temp._next = _front;
            _front._prev = temp;
            _front = _front._prev;
        }
    }

    /**
     * @param d value to be inserted in the back
     */
    public void insertBack(int d) {
        // FIXME: Implement this method
        if(_back == null){
            _front = _back = new IntDList.DNode(d);

        }
        else{
            DNode temp = new DNode(d);
            temp._prev = _back;
            _back._next = temp;
            _back = _back._next;
        }
    }

    /**
     * @param d     value to be inserted
     * @param index index at which the value should be inserted
     *              where index = 0 inserts at the front,
     *              index = 1 inserts at the second position,
     *              index = -1 inserts at the back,
     *              index = -2 inserts at the second to last position, and so on.
     *              You can assume index will always be a valid index,
     *              i.e 0 <= index <= size for positive indices (including insertions at front and back)
     *              and -(size+1) <= index <= -1 for negative indices (including insertions at front and back).
     */
    public void insertAtIndex(int d, int index) {
        // FIXME: Implement this method
        if(index == 0 || index == -(size()+1)){
            insertFront(d);
        }
        else if (index == size() || index == -1){
            insertBack(d);
        }
        else if (index > 0){
            DNode temp = _front;
            while(index != 0){
                temp = temp._next;
                index -= 1;
            }
            DNode newNode = new DNode(d);
            DNode prior = temp._prev;
            DNode post = temp;
            post._prev = newNode;
            newNode._next = post;
            prior._next = newNode;
            newNode._prev = prior;

        }
        else if (index < 0){
            DNode temp = _back;
            while(index != -2){
                temp = temp._prev;
                index += 1;
            }
            DNode newNode = new DNode(d);
            DNode prior = temp._prev;
            DNode post = temp;
            post._prev = newNode;
            newNode._next = post;
            prior._next = newNode;
            newNode._prev = prior;
        }
    }

    /**
     * Removes the first item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteFront() {
        int firstValue = _front._val;
        if(_front._next == null) _front = _back = null;
        else{
            _front = _front._next;
            _front._prev = null;
        }
        return firstValue;
    }

    /**
     * Removes the last item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteBack() {
        int finalValue = _back._val;
        if (_back._prev == null) _front = _back = null;
        else{
            _back = _back._prev;
            _back._next = null;
        }
        return finalValue;
    }

    /**
     * @param index index of element to be deleted,
     *          where index = 0 returns the first element,
     *          index = 1 will delete the second element,
     *          index = -1 will delete the last element,
     *          index = -2 will delete the second to last element, and so on.
     *          You can assume index will always be a valid index,
     *              i.e 0 <= index < size for positive indices (including deletions at front and back)
     *              and -size <= index <= -1 for negative indices (including deletions at front and back).
     * @return the item that was deleted
     */
    public int deleteAtIndex(int index) {
        // FIXME: Implement this method and return correct value
        int item = get(index);
        if(index == 0 || index == -size()){
            deleteFront();
        }
        else if (index == -1 || index == size()-1){
            deleteBack();
        }
        else if (index > 0){
            DNode pointer = _front;
            while (index > 0){
                pointer = pointer._next;
                index -= 1;
            }
            DNode prior = pointer._prev;
            DNode post = pointer._next;
            prior._next = post;
            post._prev = prior;


        }
        else{
            DNode pointer = _back;
            while (index != -1){
                pointer = pointer._prev;
                index += 1;
            }
            DNode prior = pointer._prev;
            DNode post = pointer._next;
            prior._next = post;
            post._prev = prior;
        }
        return item;
    }

    /**
     * @return a string representation of the IntDList in the form
     * [] (empty list) or [1, 2], etc.
     * Hint:
     * String a = "a";
     * a += "b";
     * System.out.println(a); //prints ab
     */
    public String toString() {
        if (_front == null) return "[]";
        else{
            String result = "[";
            DNode pointer = _front;
            while (pointer._next != null){
                result  += pointer._val + ", ";
                pointer = pointer._next;
            }
            result += pointer._val + "]";
            return result;
        }
    }

    /**
     * DNode is a "static nested class", because we're only using it inside
     * IntDList, so there's no need to put it outside (and "pollute the
     * namespace" with it. This is also referred to as encapsulation.
     * Look it up for more information!
     */
    static class DNode {
        /** Previous DNode. */
        protected DNode _prev;
        /** Next DNode. */
        protected DNode _next;
        /** Value contained in DNode. */
        protected int _val;

        /**
         * @param val the int to be placed in DNode.
         */
        protected DNode(int val) {
            this(null, val, null);
        }

        /**
         * @param prev previous DNode.
         * @param val  value to be stored in DNode.
         * @param next next DNode.
         */
        protected DNode(DNode prev, int val, DNode next) {
            _prev = prev;
            _val = val;
            _next = next;
        }
    }

}
