
/** Disjoint sets of contiguous integers that allows (a) finding whether
 *  two integers are in the same set and (b) unioning two sets together.  
 *  At any given time, for a structure partitioning the integers 1 to N, 
 *  into sets, each set is represented by a unique member of that
 *  set, called its representative.
 *  @author
 */
public class UnionFind {

    /** A union-find structure consisting of the sets { 1 }, { 2 }, ... { N }.
     */
    private int[] _parents;
    private int[] _sizes;
    public UnionFind(int N) {
        // FIXME
        _parents = new int[N + 1];
        _sizes = new int[N + 1];
        for (int i = 1; i <= N; i += 1) {
            _parents[i] = i;
            _sizes[i] = 1;
        }
    }

    /** Return the representative of the set currently containing V.
     *  Assumes V is contained in one of the sets.  */
    public int find(int v) {
        while (_parents[v] != v) {
            v = _parents[v];
        }
        return v;
    }

    /** Return true iff U and V are in the same set. */
    public boolean samePartition(int u, int v) {
        return find(u) == find(v);
    }

    /** Union U and V into a single set, returning its representative. */
    public int union(int u, int v) {
        int rootU = find(u);
        int rootV = find(v);
        int sizeU = _sizes[rootU];
        int sizeV = _sizes[rootV];
        if (sizeU >= sizeV) {
            _parents[rootU] = _parents[rootV];
            _sizes[rootV] = 0;
            _sizes[rootU] = sizeU + sizeV;
        } else {
            _parents[rootV] = _parents[rootU];
            _sizes[rootU] = 0;
            _sizes[rootV] = sizeU + sizeV;
        }
        return 0;
    }
}
