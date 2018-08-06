package tempest;

import tempest.types.Catalog;
import tempest.types.Database;
import tempest.types.Schema;
//import java.sql.*;

public class Session {
    private long startTime = 0;
    private Database database = null;
    private Catalog catalog = null;
    private Schema schema = null;

    public Session() {
        startTime = System.currentTimeMillis();
    }

    public Database getDatabase() { return database; }

    public Catalog getCatalog() { return catalog; }

    public Schema getSchema() { return schema; }

    public long getStartTime() { return startTime; }

    public void setDatabase(Database db) { database = db; }

    public void setCatalog(Catalog c) { catalog = c; }

    public void setSchema(Schema s) { schema = s; }
}