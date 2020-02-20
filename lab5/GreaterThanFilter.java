/**
 * TableFilter to filter for entries greater than a given string.
 *
 * @author Matthew Owen
 */
public class GreaterThanFilter extends TableFilter {

    public GreaterThanFilter(Table input, String colName, String ref) {
        super(input);
        _colName = colName;
        _ref = ref;
        table = input;

    }

    @Override
    protected boolean keep() {
        int index = table.colNameToIndex(_colName);
        if (_next.getValue(index).compareTo(_ref) > 0) {
            return true;
        }
        return false;
    }

    // FIXME: Add instance variables?
    private String _colName;
    private String _ref;
    private Table table;

}
