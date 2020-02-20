import java.util.Iterator;

/**
 * TableFilter to filter for entries whose two columns match.
 *
 * @author Matthew Owen
 */
public class ColumnMatchFilter extends TableFilter {

    public ColumnMatchFilter(Table input, String colName1, String colName2) {
        super(input);
        n1 = colName1;
        n2 = colName2;
        table = input;

    }

    @Override
    protected boolean keep() {
        int index1 = table.colNameToIndex(n1);
        int index2 = table.colNameToIndex(n2);
        if (_next.getValue(index1).equals(_next.getValue(index2))) {
            return true;
        }
        return false;
    }

    // FIXME: Add instance variables?
    private String n1;
    private String n2;
    private Table table;

}
