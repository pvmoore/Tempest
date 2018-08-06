package tempest.sql;

import tempest.Session;
import tempest.types.Table;

import java.sql.SQLException;

public class SQLRevoke extends SQL {

    public SQLRevoke(Session session, SQLTokenizer tz) {
        log("SQLRevoke [REVOKE " + tz + "]");
    }

    public String toString() {
        return "REVOKE[\n" +
            "" +
            "";
    }

    public Table execute() throws SQLException {
        return null;
    }
}
