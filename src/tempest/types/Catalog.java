package tempest.types;

import tempest.collections.HashList;

public class Catalog {
    final private String identifier;
    final private Database database;
    private HashList schemas     = new HashList();
    private Schema defaultSchema = null;

    //this may be the same as a database
    public Catalog(Database db, String id) {
        database = db;
        identifier = id;
    }

    //////////////////////////////////////////////////////////////////////////////
    public String getName() { return identifier; }
    public Database getDatabase() { return database; }

    public void addSchema(Schema s) {
        schemas.put(s.getName(), s);
    }

    public String toString() {
        return "" + identifier;
    }

    public Schema[] getSchemas() {
        return (Schema[])schemas.getArrayList().toArray(new Schema[0]);
    }

    public Schema getSchema(String identifier) {
        Schema s = (Schema)schemas.get(identifier);
        if(s != null) return s;
        // no schema found with this identifier so return DEFAULT schema
        if(defaultSchema == null) {
            defaultSchema = new Schema(this, "DEFAULT");
            schemas.put("DEFAULT", defaultSchema);
        }
        return defaultSchema;
    }
}