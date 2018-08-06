package tempest.sql;

import tempest.Session;
import tempest.types.Column;
import tempest.types.Table;

import java.sql.SQLException;
import java.util.ArrayList;

public class TableList {
    private Session session;
    private ArrayList<String> identifiers = new ArrayList<String>();
    private ArrayList<Table> tables = new ArrayList<Table>();

    //////////////////////////////////////////////////////////////////////////////
    public TableList(Session session) {
        this.session = session;
    }

    //////////////////////////////////////////////////////////////////////////////
    public void addTable(String identifier, String correlation) throws SQLException {
        identifier = identifier.toUpperCase();
        if(correlation != null && correlation.length() > 0) {
            correlation = correlation.toUpperCase();
            identifiers.add(correlation);
            tables.add(session.getSchema().getTable(identifier));
        } else {
            identifiers.add(identifier);
            tables.add(session.getSchema().getTable(identifier));
        }
    }

    public Table[] getTables() {
        return tables.toArray(new Table[0]);
    }

    public Column[] getColumns(String identifier) throws SQLException {
        identifier = identifier.toUpperCase();
        int index = identifiers.indexOf(identifier);
        if(index == -1) throw new SQLException("Correlation or table name '" + identifier + "' not found");
        Table t = tables.get(index);
        return t.getColumns();
    }

    public Column[] getAllColumns() throws SQLException {
        ArrayList<Column> al = new ArrayList<Column>();
        for(int i = 0; i < tables.size(); i++) {
            Table t = tables.get(i);
            Column[] cols = t.getColumns();
            for(int c = 0; cols != null && c < cols.length; c++) {
                al.add(cols[c]);
            }
        }
        return (Column[])al.toArray(new Column[0]);
    }

    public Column getColumn(String s) throws SQLException {
        s = s.toUpperCase();
        String correlation = s;
        int dot = s.indexOf('.');
        if(dot != -1) {
            // correlation name specified
            correlation = s.substring(0, dot);
            s = s.substring(dot + 1);
            int index = identifiers.indexOf(correlation);
            if(index == -1) throw new SQLException("Unknown TABLE '" + correlation + "'");
            Table table = (Table)tables.get(index);
            Column col = table.getColumn(s);
            if(col == null) throw new SQLException("Unknown column '" + s + "'");
            return col;
        } else {
            // no correlation specified so check all tables
            Column col = null;
            for(int i = 0; i < tables.size(); i++) {
                Table t = (Table)tables.get(i);
                Column col2 = t.getColumn(s);
                if(col2 != null) {
                    if(col != null) throw new SQLException("Ambiguous column reference '" + s + "'");
                    col = col2;
                }
            }
            if(col == null) throw new SQLException("Column not found '" + s + "'");
            return col;
        }
    }
}