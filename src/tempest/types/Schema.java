package tempest.types;

import tempest.collections.HashList;

import java.sql.SQLException;

public class Schema {
    private String identifier = null;
    private HashList tables = new HashList();
    private HashList views = new HashList();
    private Catalog catalog = null;

    public Schema(Catalog c, String identifier) {
        catalog = c;
        this.identifier = identifier;
    }

    //////////////////////////////////////////////////////////////////////////////
    public String getName() { return identifier; }

    public Catalog getCatalog() { return catalog; }

    //
    public void addTable(Table t) throws SQLException {
        if(tables.containsKey(t.getName())) {
            throw new SQLException("Table '" + t.getName() + "' already exists in Schema '" + this + "'");
        }
        tables.put(t.getName(), t);
    }

    public Table getTable(String identifier) throws SQLException {
        //System.out.println("schema looking for table '"+identifier+"' : "+tables);
        if(identifier != null && !identifier.startsWith("\"")) {
            identifier = identifier.toUpperCase();
        }
        if(identifier == null || !tables.containsKey(identifier)) {
            throw new SQLException("Table '" + identifier + "' not found in Schema '" + this + "'");
        }
        return (Table)tables.get(identifier);
    }

    public Table[] getTables() {
        return (Table[])tables.getArrayList().toArray(new Table[0]);
    }

    public String toString() {
        return "" + identifier;
    }

    public Table createTable(String identifier) throws SQLException {
        if(identifier == null) throw new SQLException("Table name is null");
        if(!identifier.startsWith("\"")) {
            identifier = identifier.toUpperCase();
        }
        //if(tables.containsKey(identifier)) {
        //throw new SQLException("TABLE '"+identifier+"' already exists in Schema '"+this+"'");
        //}
        Table t = null;
        switch(catalog.getDatabase().getType()) {
            case Database.MEMORY:
                t = new MemoryTable(identifier);
                break;
            case Database.LOCALFILE:
                break;
        }
        //tables.put(identifier,t);
        return t;
    }
}