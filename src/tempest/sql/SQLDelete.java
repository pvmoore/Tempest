package tempest.sql;

import tempest.Session;
import tempest.types.Table;

import java.sql.SQLException;

public class SQLDelete extends SQL {

    public SQLDelete(Session session, SQLTokenizer tz) {
        log("SQLDelete [DELETE " + tz + "]");
    }

    public String toString() {
        return "DELETE[\n" +
            "" +
            "";
    }

    public Table execute() throws SQLException {
        return null;
    }
}