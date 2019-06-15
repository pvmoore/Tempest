package tempest.types;

import tempest.Session;
import tempest.data._ROW;

import java.sql.SQLException;
import java.util.ArrayList;

public class MemoryTable extends Table {
    // protected :
    //   String identifier
    //   Hashtable columns = new Hashtable();
    private ArrayList rows = new ArrayList();  // Row instances

    protected MemoryTable(String identifier) {
        super(identifier);
        // initialise here
        // set rows if any exist
        // set columns if this table already exists
        // otherwise this is a newly created, empty table
    }

    public String toString() {
        StringBuilder buf = new StringBuilder("TABLE " + identifier + " [\n");
        for(int i = 0; columns != null && i < columns.length; i++) {
            buf.append(columns[i]).append("\n");
        }
        buf.append("] ").append(rows.size()).append(" rows");
        return buf.toString();
    }

    /////////////////////////////////////////////////////////// overloaded methods
    public void addColumn(Column c) throws SQLException {
        super.addColumn(c);
        //todo alter all Rows in this table to have more cols?
    }

    ///////////////////////////////////////////////////////////// abstract methods
    public int getCardinality() { return rows.size(); }

    public _ROW getRow(int index) throws SQLException {
        return (_ROW)rows.get(index);
    }

    //public void updateRow(int index,_ROW row) throws SQLException {
    //  ((_ROW)rows.get(index)).set(row);
    //}
    //public void updateRow(int index,TypedObject[] data) throws SQLException  {
    //  ((_ROW)rows.get(index)).set(data);
    //}
    public void addRow(_ROW row, Session session) throws SQLException {
        rows.add(row);
        //Engine.getLogger().log("addRow : "+row);
    }

    //public void insertRow(TypedObject[] data) throws SQLException {
    //  _ROW r=new _ROW(this,data);
    //  rows.add(r);
    //}
    public void deleteRow(int index, Session session) throws SQLException {
        if(index >= rows.size()) throw new SQLException("Can't delete row " + index);
        rows.remove(index);
    }

    public void truncate() {
        rows.clear();
    }

    public void commit(Session session) throws SQLException {

    }

    public void rollback(Session session) throws SQLException {

    }

    public boolean equals(Table t) throws SQLException {
        int cardinality = getCardinality();
        int degree = getDegree();
        if(t.getCardinality() != cardinality) {
            //System.out.println("cardinality!=");
            return false;
        }
        if(t.getDegree() != degree) {
            //System.out.println("degree!=");
            return false;
        }
        for(int i = 0; i < degree; i++) {
            if(!columns[i].equals(t.getColumn(i))) {
                //System.out.println("col!=");
                return false;
            }
        }
        for(int i = 0; i < cardinality; i++) {
            if(!rows.get(i).equals(t.getRow(i))) {
                //System.out.println("row!=");
                return false;
            }
        }
        return true;
    }
    //////////////////////////////////////////////////////////////////////////////
}