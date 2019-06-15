package tempest.types;

import tempest.Session;
import tempest.data._ROW;

import java.sql.SQLException;

public abstract class Table {
    final protected String identifier;
    protected Column[] columns = null;

    protected Table(String identifier) {
        this.identifier = identifier;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer("TABLE " + identifier + " [\n");
        for(int i = 0; columns != null && i < columns.length; i++) {
            buf.append("" + columns[i] + "\n");
        }
        buf.append("]");
        return buf.toString();
    }

    public synchronized void addColumn(Column c) throws SQLException {
        //System.out.println("addColumn("+c+")");
        for(int i = 0; columns != null && i < columns.length; i++) {
            if(columns[i].getName().equals(c.getName())) {
                throw new SQLException("TABLE " + identifier + " already contains a column called '" + c
                    .getName() + "'");
            }
        }
        if(columns == null) {
            columns = new Column[1];
            columns[0] = c;
        } else {
            Column[] temp = new Column[columns.length + 1];
            System.arraycopy(columns, 0, temp, 0, columns.length);
            temp[columns.length] = c;
            columns = temp;
        }
    }

    public void renameColumn(String from, String to) {
    }

    public void removeColumn(String name) {
    }

    public _ROW createRow() {
        return new _ROW(this);
    }

    public String getName() { return identifier; }

    public int getDegree() {
        if(columns == null) return 0;
        return columns.length;
    }

    public Column[] getColumns() { return columns; }

    public String getIdentifier() { return identifier; }

    public int getIndexOfColumn(String identifier) throws SQLException {
        identifier = identifier.toUpperCase();
        for(int i = 0; columns != null && i < columns.length; i++) {
            if(identifier.equals(columns[i].getName())) return i;
        }
        throw new SQLException("Column '" + identifier + "' not found in table '" + this.identifier + "'");
    }

    public Column getColumn(int index) throws SQLException {
        return columns[index];
    }

    public Column getColumn(String identifier) throws SQLException {
        identifier = identifier.toUpperCase();
        for(int i = 0; columns != null && i < columns.length; i++) {
            if(identifier.equals(columns[i].getName())) return columns[i];
        }
        throw new SQLException("Column '" + identifier + "' not found in table '" + this.identifier + "'");
    }

    //////////////////////////////////////////////////////////////////////////////
    abstract public boolean equals(Table t) throws SQLException;

    abstract public void truncate();

    abstract public int getCardinality();

    abstract public void commit(Session session) throws SQLException;

    abstract public void rollback(Session session) throws SQLException;

    abstract public _ROW getRow(int index) throws SQLException;

    abstract public void addRow(_ROW row, Session session) throws SQLException;

    // maybe replace with deleteRow(_ROW row,Session session) throws SQLException;
    abstract public void deleteRow(int index, Session session) throws SQLException;
    //abstract public void updateRow(int index,_ROW data) throws SQLException;
    //abstract public void updateRow(int index,TypedObject[] data) throws SQLException;
    //abstract public void insertRow(TypedObject[] data) throws SQLException;
    //////////////////////////////////////////////////////////////////////////////
}