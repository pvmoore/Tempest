package tempest.sql;

import tempest.Session;
import tempest.types.Table;

import java.sql.SQLException;

public class SQLUpdate extends SQL {

    public SQLUpdate(Session session, SQLTokenizer tz) {
        log("SQLUpdate [UPDATE " + tz + "]");
    }

    public String toString() {
        return "INSERT[\n" +
            "" +
            "]";
    }

    public Table execute() throws SQLException {
        return null;
    }
}