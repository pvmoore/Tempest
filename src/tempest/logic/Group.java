package tempest.logic;

import tempest.data._ROW;

import java.sql.SQLException;
import java.util.ArrayList;

public class Group {
    private ArrayList<_ROW> rows = new ArrayList<_ROW>();
    //private TableList tableList       = null;

    public Group() {}

    //public void setTableList(TableList tableList) {
    //  this.tableList = tableList;
    //}
    public int size() {
        if(rows == null) return 0;
        return rows.size();
    }

    public void addRow(_ROW row) throws SQLException {
        rows.add(row);
    }

    public _ROW getRow(int index) {
        return rows.get(index);
    }
}