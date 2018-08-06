package tempest.logic;

import tempest.data._INT;
import tempest.data._ROW;
import tempest.interfaces.TypedObject;
import tempest.interfaces.ValueExpression;
import tempest.sql.Keyword;
import tempest.sql.SQLTokenizer;
import tempest.sql.TableList;
import tempest.types.Column;

import java.sql.SQLException;
import java.util.Hashtable;

public class CountValueExpression implements ValueExpression {
    public static final int ALL = 0;
    public static final int DISTINCT = 1; // distinct column
    public static final int COLUMN = 2;

    private int type = -1;
    private Column column = null;
    private boolean distinct = false;
    private boolean all = false;

    public CountValueExpression() { System.out.println("CountValueExpression");}

    public TypedObject evaluate(Group grp) throws SQLException {
        //System.out.println("evaluate group type = "+type);
        switch(type) {
            case ALL:
                return new _INT(grp.size());
            case DISTINCT: {
                Hashtable<TypedObject, String> hash = new Hashtable<TypedObject, String>();
                for(int i = 0; i < grp.size(); i++) {
                    TypedObject tobj = grp.getRow(i).get(column);
                    if(tobj != null) {
                        hash.put(tobj, "");
                    }
                }
                return new _INT(hash.size());
            }
            case COLUMN: {
                int count = 0;
                for(int i = 0; i < grp.size(); i++) {
                    if(grp.getRow(i).get(column) != null) count++;
                }
                return new _INT(count);
            }
        }
        return new _INT(0);
    }

    public TypedObject evaluate(_ROW row) throws SQLException {
        if(all) return new _INT(ALL);
        if(distinct) return new _INT(DISTINCT);
        return new _INT(COLUMN);
    }

    // possible args are:
    // count(*)
    // count([DISTINCT] col)
    // count([ALL] col)
    public void parseExpression(SQLTokenizer tz, TableList tableList) throws SQLException {
        if(tz.countTokens() == 0) throw new SQLException("Syntax error");
        if(tz.getType(0) == Keyword.ALL) {
            tz = tz.split(1);
        }
        if(tz.getType(0) == Keyword.DISTINCT) {
            type = DISTINCT;
            distinct = true;
            if(tz.countTokens() < 2) throw new SQLException("Syntax error at DISTINCT");
            column = tableList.getColumn(tz.getToken(1));
            if(column == null) throw new SQLException("Column '" + tz.getToken(1) + "' not found");
        } else if(tz.getType(0) == SQLTokenizer.MUL) {
            type = ALL;
            all = true;
            if(tz.countTokens() != 1) throw new SQLException("Syntax error");
        } else {
            type = COLUMN;
            column = tableList.getColumn(tz.getToken(0));
            if(column == null) throw new SQLException("Column '" + tz.getToken(1) + "' not found");
        }
    }

    public TypedObject simplifyExpression() {
        return null;
    }

    public int getReturnType() { return TypedObject.INT; }

    public Column getColumn() {
        return column;
    }

    public String displayExpression(String pad) {
        StringBuffer buf = new StringBuffer(pad + "COUNT(");
        if(distinct) buf.append("DISTINCT ");
        if(all) buf.append("*");
        if(column != null) buf.append(column.toString());
        buf.append(")");
        return buf.toString();
    }

    public boolean isAggregate() { return true; }
}