package tempest.sql;

import tempest.Session;
import tempest.collections.HashList;
import tempest.data.DataFactory;
import tempest.data._ROW;
import tempest.interfaces.TypedObject;
import tempest.types.Column;
import tempest.types.Table;

import java.sql.SQLException;
import java.util.ArrayList;

/*
  insert [into] <table|view>
     [(column[,column...])]
  {  [VALUES (value[,value...]) ]  |  [SELECT <select_list>]  }
*/
public class SQLInsert extends SQL {
    private HashList columns = new HashList();  // Column instances
    private ArrayList<String> values = new ArrayList<String>();
    private Table destination = null;
    private String select = null;
    private Session session = null;
    private String rawSQL = null;
    private SQLTokenizer tz = null;

    public SQLInsert(Session session, SQLTokenizer tz) throws SQLException {
        this.session = session;
        this.tz = tz;
        log("***********************************************************************");
        log("SQLInsert [INSERT " + tz + "]");
        log("***********************************************************************");
        if("INTO".equals(tz.getTokenUpper(0))) tz = tz.split(1);
        destination = session.getSchema().getTable(tz.getToken(0));
        //if(destination==null) {
        //  throw new SQLException("Table/View "+tz.getToken()+" does not exist");
        //}
        tz = tz.split(1);
        int selectPos = tz.indexOf(Keyword.SELECT);
        int valuesPos = tz.indexOf(Keyword.VALUES);
        //System.out.println("selectPos="+selectPos);
        //System.out.println("valuesPos="+valuesPos);
        if(selectPos == -1 && valuesPos == -1) {
            throw new SQLException("Syntax error");
        }
        if(selectPos != -1 && valuesPos != -1) {
            throw new SQLException("Syntax error");
        }
        if(selectPos != -1) {
            // todo
            SQLTokenizer sel = tz.split(selectPos);
            tz = tz.split(0, selectPos - 1); //tz.setLast(selectPos-1);
            throw new SQLException("SELECT not yet supported in INSERT");
        }
        int br1 = tz.indexOf(SQLTokenizer.LEFTBRACKET);
        int br2 = tz.lastIndexOf(SQLTokenizer.RIGHTBRACKET, valuesPos);
        //System.out.println("br1="+br1);
        //System.out.println("br2="+br2);
        if(br2 != -1) {
            //System.out.println("there are columns specified");
            SQLTokenizer tz2 = tz.split(br1 + 1, br2 - 1);
            SQLTokenizer[] tzs = tz2.parse(SQLTokenizer.COMMA);
            for(int i = 0; tzs != null && i < tzs.length; i++) {
                if(tzs[i].countTokens() == 0) {
                    throw new SQLException("Syntax error at ','");
                }
                //System.out.println("column specified: "+tzs[i].getToken());
                columns.put(tzs[i].getToken(0), destination.getColumn(tzs[i].getToken(0)));
            }
        } else {
            //System.out.println("there are no columns specified");
            Column[] cols = destination.getColumns();
            for(int i = 0; cols != null && i < cols.length; i++) {
                columns.put(cols[i].getName(), cols[i]);
            }
        }
        //
        if(valuesPos != -1) {
            tz = tz.split(valuesPos + 1);
            br1 = tz.indexOf(SQLTokenizer.LEFTBRACKET);
            br2 = tz.lastIndexOf(SQLTokenizer.RIGHTBRACKET);
            if(br1 == -1 || br2 == -1) throw new SQLException("Syntax error");
            SQLTokenizer tz2 = tz.split(br1 + 1, br2 - 1);
            SQLTokenizer[] tzs = tz2.parse(SQLTokenizer.COMMA);
            // TODO - here follows a load of nonsense. fix it later
            for(int i = 0; tzs != null && i < tzs.length; i++) {
                Column col = (Column)columns.get(i);
                String str = tzs[i].merge(" ");
                if(col.getLength() != Integer.MAX_VALUE && str.length() > col.getLength() + 2) {
                    // add 2 chars for quotes
                    throw new SQLException("Value is too long for column " +
                                               col.getName() + "(" + col.getLength() + ")");
                    //str = str.substring(0,cols[i].getLength());
                }
                values.add(str);
            }
            if(columns.size() > 0 && columns.size() != values.size()) {
                throw new SQLException("Incorrect number of columns/values");
            }
        } else {
            throw new SQLException("SELECT not yet supported in INSERT");
        }
        //
        log("insert=" + this);
    }

    public String toString() {
        return "INSERT[\n" +
            "INTO " + destination + "\n" +
            "" + columns + "\n" +
            "" + values + "\n" +
            "]";
    }

    public Table execute() throws SQLException {
        // if this is not a table do something else
        Object[] objs = new Object[destination.getDegree()];
        _ROW r = new _ROW(destination);
        for(int i = 0; i < columns.size(); i++) {
            Column c = (Column)columns.get(i);
            String s = values.get(i);
            TypedObject to = DataFactory.createTypedObject(c.getType(), new SQLTokenizer(s));
            //System.out.println("setting "+c+" to "+to);
            r.set(c, to);
        }
        destination.addRow(r, session);
        return destination;
    }
}