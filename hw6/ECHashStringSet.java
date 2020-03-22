import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** A set of String values.
 *  @author Zachary Zhu
 */
class ECHashStringSet implements StringSet {
    // Load Factor = # items/ # buckets
    private double loadFactorMin = 0.2;
    private double loadFactorMax = 5;
    private LinkedList<String>[] buckets;
    private int numItems;

    public ECHashStringSet () {
        buckets = new LinkedList[4];
        for (int i = 0; i < buckets.length; i += 1) {
            buckets[i] = new LinkedList<String>();
        }
        numItems = 0;
    }


    private int hashCodeConverter(String s) {
        return (s.hashCode() & 0x7fffffff) % buckets.length;
    }

    private void resize() {
        LinkedList<String> [] new_buckets = buckets;
        buckets = new LinkedList[new_buckets.length * 5];
        for (int i = 0; i < buckets.length; i += 1) {
            buckets[i] = new LinkedList<String>();
        }
        numItems = 0;
        for (int i = 0; i < buckets.length; i += 1) {
            for (String s : buckets[i]) {
                put(s);
            }
        }
    }

    @Override
    public void put(String s) {
        if (numItems / buckets.length > loadFactorMax) {
            resize();
        }
        int hashCode = hashCodeConverter(s);
        buckets[hashCode].add(s);
        numItems += 1;
    }

    @Override
    public boolean contains(String s) {
        return buckets[hashCodeConverter(s)].contains(s);
    }

    @Override
    public List<String> asList() {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < buckets.length; i += 1) {
            for (String s : buckets[i]) {
                result.add(s);
            }
        }
        return result;
    }
}
