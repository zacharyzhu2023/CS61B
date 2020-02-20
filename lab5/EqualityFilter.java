/**
 * TableFilter to filter for entries equal to a given string.
 *
 * @author Matthew Owen
 */
public class EqualityFilter extends TableFilter {

    public EqualityFilter(Table input, String colName, String match) {
        super(input);
        _colName = colName;
        _match = match;
        table = input;
        // FIXME: Add your code here.

    }

    @Override
    protected boolean keep() {
        int index = table.colNameToIndex(_colName);
        if (_next.getValue(index).equals(_match)) {
            return true;
        }
        return false;
    }

    // FIXME: Add instance variables?
    String _colName;
    String _match;
    Table table;

}
