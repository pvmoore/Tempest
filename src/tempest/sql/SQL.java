package tempest.sql;

import tempest.Engine;
import tempest.Session;
import tempest.types.Table;

import java.sql.SQLException;

public abstract class SQL {

    public static void log(String s) {
        Engine.getLogger().log(s);
    }

    public static SQL getSQL(Session session, String s) throws SQLException {
        //log("SQL="+s);
        SQLTokenizer tz = new SQLTokenizer(s);
        //System.out.println(""+tz);
        if(tz.countTokens() == 0) {
            throw new SQLException("No SQL to process");
        }
        //if(s==null || s.trim().length()==0) throw new SQLException("No SQL to process");
        //s = s.trim();
        //String upper = s.toUpperCase();
        String token = tz.getToken(0);
        tz = tz.split(1);
        if("SELECT".equals(token)) {
            return new SQLSelect(session, tz);
        } else if("CREATE".equals(token)) {
            return new SQLCreate(session, tz);
        } else if("INSERT".equals(token)) {
            return new SQLInsert(session, tz);
        } else if("UPDATE".equals(token)) {
            return new SQLUpdate(session, tz);
        } else if("DROP".equals(token)) {
            return new SQLDrop(session, tz);
        } else if("DELETE".equals(token)) {
            return new SQLDelete(session, tz);
        } else if("REVOKE".equals(token)) {
            return new SQLRevoke(session, tz);
        } else if("GRANT".equals(token)) {
            return new SQLGrant(session, tz);
        } else {
            log("Unrecognised SQL command [" + token + "]");
            throw new SQLException("Unrecognised SQL statement [" + token + "]");
        }
    }

    public abstract Table execute() throws SQLException;
}