package tempest.sql;

import tempest.Session;
import tempest.types.Table;

import java.sql.SQLException;

public class SQLGrant extends SQL {

    public SQLGrant(Session session, SQLTokenizer tz) {
        log("SQLGrant [GRANT " + tz + "]");
    }

    public String toString() {
        return "GRANT[\n" +
            "" +
            "";
    }

    public Table execute() throws SQLException {
        return null;
    }
}