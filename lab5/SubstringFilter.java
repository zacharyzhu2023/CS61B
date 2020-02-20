/**
 * TableFilter to filter for containing substrings.
 *
 * @author Matthew Owen
 */
public class SubstringFilter extends TableFilter {

    public SubstringFilter(Table input, String colName, String subStr) {
        super(input);
        table = input;
        _colName = colName;
        _subStr = subStr;
    }

    @Override
    protected boolean keep() {
        int index = table.colNameToIndex(_colName);
        if (_next.getValue(index).contains(_subStr)) {
            return true;
        }
        return false;
    }

    // FIXME: Add instance variables?
    private Table table;
    private String _colName;
    private String _subStr;
}
