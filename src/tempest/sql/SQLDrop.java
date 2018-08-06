package tempest.sql;

import tempest.Session;
import tempest.types.Table;

import java.sql.SQLException;

public class SQLDrop extends SQL {

    public SQLDrop(Session session, SQLTokenizer tz) {
        log("SQLDrop [DROP " + tz + "]");
    }

    public String toString() {
        return "DROP[\n" +
            "" +
            "";
    }

    public Table execute() throws SQLException {
        return null;
    }
}