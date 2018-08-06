package tempest.sql;

import tempest.Session;
import tempest.Tools;
import tempest.interfaces.TypedObject;
import tempest.types.Column;
import tempest.types.Table;

import java.sql.SQLException;
import java.util.Hashtable;

public class SQLCreate extends SQL {
    private Session session = null;
    private String object = null;
    private String identifier = null;
    private Table table = null;
    private SQLTokenizer tz = null;

    public SQLCreate(Session session, SQLTokenizer tokens) throws SQLException {
        this.session = session;
        this.tz = tokens;
        log("***********************************************************************");
        log("SQLCreate [CREATE " + tz + "]");
        log("***********************************************************************");
        //
        String token = tz.getToken(0);
        tz = tz.split(1);
        if("TABLE".equals(token)) {
            object = "TABLE";
            createTable();
        } else if("USER".equals(token)) {
            throw new SQLException("USERS NOT CURRENTLY SUPPORTED");
        } else if("ROLE".equals(token)) {
            throw new SQLException("ROLES NOT CURRENTLY SUPPORTED");
        } else if("INDEX".equals(token)) {
            throw new SQLException("INDICES NOT CURRENTLY SUPPORTED");
        } else if("VIEW".equals(token)) {
            throw new SQLException("VIEWs NOT CURRENTLY SUPPORTED");
        } else if("SCHEMA".equals(token)) {
            throw new SQLException("SCHEMA CREATION NOT CURRENTLY SUPPORTED");
        } else if("DATABASE".equals(token)) {
            throw new SQLException("DATABASE CREATION NOT CURRENTLY SUPPORTED");
        } else if("CATALOG".equals(token)) {
            throw new SQLException("CATALOG CREATION NOT CURRENTLY SUPPORTED");
        } else {
            throw new SQLException("Parse error");
        }
    }

    public String toString() {
        return "CREATE[\n" +
            "  " + object + "\n" +
            "  " + identifier + "\n" +
            "]";
    }

    private void createTable() throws SQLException {
        //System.out.println("createTable : "+tz);
        int p1 = tz.indexOf(SQLTokenizer.LEFTBRACKET);
        int p2 = tz.lastIndexOf(SQLTokenizer.RIGHTBRACKET);
        if(p1 == -1 || p2 == -1) throw new SQLException("Syntax error");
        //
        identifier = tz.getToken(0);
        table = session.getSchema().createTable(identifier);
        //
        SQLTokenizer columnList = tz.split(p1 + 1, p2 - 1);
        //System.out.println("columnList="+columnList);
        SQLTokenizer[] list = columnList.parse(SQLTokenizer.COMMA);
        //System.out.println("list.length="+list.length);
        for(int i = 0; i < list.length; i++) {
            table.addColumn(parseColumn(list[i]));
        }
        log(table.toString());
    }

    /**
     * <identifier> <type>
     * [NULL | NOT NULL | UNIQUE | DEFAULT (<defaultvalue>) | PRIMARY KEY]
     */
    public Column parseColumn(SQLTokenizer t) throws SQLException {
        System.out.println("parseColumn(t)=" + t);
        if(t.countTokens() == 0) throw new SQLException("Syntax error");
        String colIdentifier = t.getToken(0);
        String typeStr = t.getTokenUpper(1);
        t = t.split(2);
        int type = -1;
        if("INTERVAL".equals(typeStr)) {
            throw new SQLException("TODO");
        } else {
            Integer n = typeHash.get(typeStr);
            if(n != null) {
                type = n.intValue();
            }
        }
        Column col = new Column(colIdentifier, type);
        //
        for(int i = 0; i < t.countTokens(); i++) {
            String token = t.getTokenUpper(i);
            try {
                if("NULL".equals(token)) {
                    if("NOT".equals(t.getTokenUpper(i - 1))) {
                        col.setNullable(false);
                    } else
                        throw new SQLException();
                } else if("NOT".equals(token)) {
                    if(!"NULL".equals(t.getTokenUpper(i + 1)))
                        throw new SQLException();
                } else if("DEFAULT".equals(token)) {
                    if(t.getType(i + 1) == SQLTokenizer.LEFTBRACKET) {
                        i += 2;
                        col.setDefaultValue(t.split(i));
                    } else {
                        i++;
                        col.setDefaultValue(t.split(i));
                    }
                } else if("PRIMARY".equals(token)) {
                    if(!"KEY".equals(t.getTokenUpper(i + 1)))
                        throw new SQLException();
                } else if("KEY".equals(token)) {
                    if("PRIMARY".equals(t.getTokenUpper(i - 1))) {
                        col.setPrimaryKey(true);
                    } else
                        throw new SQLException();
                } else if("UNIQUE".equals(token)) {
                    col.setUnique(true);
                } else if("INTERVAL".equals(token)) {
                    if("YEAR*MONTH*DAY*HOUR*MINUTE*SECOND".indexOf(t.getTokenUpper(i + 1)) == -1) {
                        throw new SQLException();
                    }
                } else if("TO".equals(token)) {
                    if("MONTH*DAY*HOUR*MINUTE*SECOND".indexOf(t.getTokenUpper(i + 1)) == -1) {
                        throw new SQLException();
                    }
                } else if("YEAR".equals(token)) {
                    col.setType(TypedObject.YMINTERVAL);
                } else if("MONTH".equals(token)) {
                    col.setType(TypedObject.YMINTERVAL);
                } else if("HOUR".equals(token)) {
                    col.setType(TypedObject.DTINTERVAL);
                } else if("MINUTE".equals(token)) {
                    col.setType(TypedObject.DTINTERVAL);
                } else if("SECOND".equals(token)) {
                    col.setType(TypedObject.DTINTERVAL);
                } else if("(".equals(token)) {
                    i++;
                    int num = Tools.parseInt(t.getToken(i), Integer.MIN_VALUE);
                    if(num == Integer.MIN_VALUE) throw new SQLException("Bad length " + t.getToken(i));
                    col.setLength(num);
                    i++;
                } else {
                    throw new SQLException("Unknown");
                }
            } catch(Exception e) {
                throw new SQLException("Syntax error at '" + token + "' " + e.getMessage());
            }
        }
        //System.out.println("col="+col);
        if(col.getType() == -1) {
            throw new SQLException("" + colIdentifier + " has invalid type");
        }
        return col;
    }

    public Table execute() throws SQLException {
        session.getSchema().addTable(table);
        //System.out.println("table="+table);
        return table;
    }

    private static final Hashtable<String, Integer> typeHash = new Hashtable<String, Integer>();

    static {
        typeHash.put("TEXT", TypedObject.STRING);
        typeHash.put("STRING", TypedObject.STRING);
        typeHash.put("LONGVARCHAR", new Integer(TypedObject.STRING));
        typeHash.put("VARCHAR", new Integer(TypedObject.STRING));
        typeHash.put("VARCHAR2", new Integer(TypedObject.STRING));
        typeHash.put("CHAR", new Integer(TypedObject.STRING));
        typeHash.put("CHARACTER", new Integer(TypedObject.STRING));
        typeHash.put("AUTONUMBER", new Integer(TypedObject.INT));
        typeHash.put("SERIAL", new Integer(TypedObject.INT));
        typeHash.put("AUTOINCREMENT", new Integer(TypedObject.INT));
        typeHash.put("IDENTITY", new Integer(TypedObject.INT));
        typeHash.put("INT", new Integer(TypedObject.INT));
        typeHash.put("INT4", new Integer(TypedObject.INT));
        typeHash.put("INT8", new Integer(TypedObject.LONG));
        typeHash.put("FLOAT4", new Integer(TypedObject.FLOAT));
        typeHash.put("FLOAT8", new Integer(TypedObject.DOUBLE));
        typeHash.put("INTEGER", new Integer(TypedObject.INT));
        //typeHash.put("BYTE",          new Integer(TypedObject.BYTE));
        //typeHash.put("TINYINT",       new Integer(TypedObject.BYTE));
        typeHash.put("LONG", new Integer(TypedObject.LONG));
        typeHash.put("BIGINT", new Integer(TypedObject.LONG));
        typeHash.put("SMALLINT", new Integer(TypedObject.INT));
        typeHash.put("DOUBLE", new Integer(TypedObject.DOUBLE));
        typeHash.put("FLOAT", new Integer(TypedObject.FLOAT));
        typeHash.put("REAL", new Integer(TypedObject.DOUBLE));
        typeHash.put("DATE", new Integer(TypedObject.DATE));
        typeHash.put("DATETIME", new Integer(TypedObject.TIMESTAMP));
        typeHash.put("TIMESTAMP", new Integer(TypedObject.TIMESTAMP));
        typeHash.put("TIME", new Integer(TypedObject.TIME));
        typeHash.put("BIT", new Integer(TypedObject.BOOLEAN));
        typeHash.put("BOOLEAN", new Integer(TypedObject.BOOLEAN));
        typeHash.put("BOOL", new Integer(TypedObject.BOOLEAN));
        typeHash.put("INTERVALYEARTOMONTH", new Integer(TypedObject.YMINTERVAL));
        typeHash.put("INTERVALYEAR", new Integer(TypedObject.YMINTERVAL));
        typeHash.put("INTERVALMONTH", new Integer(TypedObject.YMINTERVAL));
        typeHash.put("INTERVALDAYTOSECOND", new Integer(TypedObject.YMINTERVAL));
        typeHash.put("INTERVALDAYTOMINUTE", new Integer(TypedObject.YMINTERVAL));
        typeHash.put("INTERVALDAYTOHOUR", new Integer(TypedObject.YMINTERVAL));
        typeHash.put("INTERVALSECOND", new Integer(TypedObject.YMINTERVAL));
        typeHash.put("INTERVALMINUTETOSECOND", new Integer(TypedObject.YMINTERVAL));
    }
}