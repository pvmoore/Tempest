package tempest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import tempest.collections.HashList;
import tempest.types.Database;

import java.io.File;
import java.sql.SQLException;

//import tempest.logic.*;

/**
 * completely static class
 */
public class Engine {
    private static Log log = LogFactory.getLog(Engine.class);

    public static final String VERSION = "0.131";
    public static int COMPARE_TO_NULL = 1; // 1 = low, -1 = high (need to change SortList if this is -1)

    private static boolean engineReady = false;
    private static String rootPath = ".";
    private static HashList databases = new HashList();
    private static Logger logger = null;

    public static void start(String path) {
        if(engineReady) {
            log.info("Tempest Engine already running. Ignoring start request");
            System.out.println("Tempest Engine already running. Ignoring start request");
            return;
        }
        if(path != null) {
            rootPath = new File(path).getAbsolutePath();
            if(!rootPath.endsWith(File.separator)) rootPath += File.separator;
        }
        logger = getLogger();
        logger.log("Tempest Engine v" + VERSION + " starting with root path of " + rootPath);
        readDatabases();
        engineReady = true;
        logger.log("Ready...");
    }

    public static void destroy() {
        engineReady = false;
        log.info("Tempest Engine closing Databases");
        logger.log("Tempest Engine closing Databases");
        for(int i = 0; databases != null && i < databases.size(); i++) {
            ((Database)databases.get(i)).close();
        }
        log.info("Tempest Engine destroyed");
        logger.close();
    }

    ////////////////////////////////////////////////////////////////////////////// getters
    public static Logger getLogger() {
        if(logger == null) {
            logger = new Logger(".logs/dblog.txt");
        }
        return logger;
    }

    public static boolean isEngineReady() { return engineReady; }

    public static String getRootPath() { return rootPath; }

    public static Database getDatabase(String name, String path, int type) throws SQLException {
        name = ("" + name).toUpperCase();
        if(!databases.containsKey("" + type + name)) {
            throw new SQLException("Database '" + name + "' does not exist");
        }
        return (Database)databases.get("" + type + name);
    }

    public static Database getDatabase(String name) throws SQLException {
        name = ("" + name).toUpperCase();
        Database db = (Database)databases.get(name);
        if(db == null) throw new SQLException("Cannot find Database '" + name + "'");
        return db;
    }

    public static Database createDatabase(String name, String path, int type) throws SQLException {
        if(name == null) throw new SQLException("Cannot create Database '" + name + "'");
        name = name.toUpperCase();
        if(databases.containsKey(name)) throw new SQLException("Database '" + name + "' already exists");
        Database db = new Database(name, path, type);
        databases.put(name, db);
        return db;
    }

    //////////////////////////////////////////////////////////////////////////////
    private static void readDatabases() {
        // todo
    }
}
