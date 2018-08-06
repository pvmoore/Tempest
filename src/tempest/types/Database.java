package tempest.types;

import tempest.Engine;
import tempest.collections.HashList;
//import tempest.stores.*;

public class Database {
    public static final int MEMORY = 0;
    public static final int LOCALFILE = 1;
    //
    //private static Hashtable databases = new Hashtable();
    //
    private int type = MEMORY;
    private String name = null;
    private boolean available = false;
    private HashList catalogs = new HashList();
    private Catalog defaultCatalog = null;

    public Database(String name, String path, int type) {
        Engine.getLogger().log("new Database(" + name + "," + path + ")");
        this.type = type;
        this.name = name;
    }

    //////////////////////////////////////////////////////////////////////////////
    public void addCatalog(Catalog c) {
        catalogs.put(c.getName(), c);
    }

    public Catalog getCatalog(String identifier) {
        Catalog c = (Catalog)catalogs.get(identifier);
        if(c != null) return c;
        // catalog not found so return DEFAULT Catalog
        if(defaultCatalog == null) {
            defaultCatalog = new Catalog(this, "DEFAULT");
            catalogs.put("DEFAULT", defaultCatalog);
        }
        return defaultCatalog;
    }

    //////////////////////////////////////////////////////////////////////////////
    public int getType() { return type; }

    public boolean isAvailable() { return available; }

    public String getName() { return name; }

    public boolean open() {
        available = true;
        Engine.getLogger().log("opening Database " + name);
        return available;
    }

    public void close() {
        available = false;
        Engine.getLogger().log("closing Database " + name);
    }
}