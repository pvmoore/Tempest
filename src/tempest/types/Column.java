package tempest.types;

import tempest.data.DataFactory;
import tempest.interfaces.TypedObject;
import tempest.sql.SQLTokenizer;

import java.sql.SQLException;

public class Column {
    //
  /*
  private static Hashtable typeHash = new Hashtable();
  static{
    typeHash.put("TEXT",          new Integer(TypedObject.STRING));
    typeHash.put("STRING",        new Integer(TypedObject.STRING));
    typeHash.put("LONGVARCHAR",   new Integer(TypedObject.STRING));
    typeHash.put("VARCHAR",       new Integer(TypedObject.STRING));
    typeHash.put("VARCHAR2",      new Integer(TypedObject.STRING));
    typeHash.put("CHAR",          new Integer(TypedObject.STRING));
    typeHash.put("CHARACTER",     new Integer(TypedObject.STRING));
    typeHash.put("AUTONUMBER",    new Integer(TypedObject.INT));
    typeHash.put("SERIAL",        new Integer(TypedObject.INT));
    typeHash.put("AUTOINCREMENT", new Integer(TypedObject.INT));
    typeHash.put("IDENTITY",      new Integer(TypedObject.INT));
    typeHash.put("INT",           new Integer(TypedObject.INT));
    typeHash.put("INTEGER",       new Integer(TypedObject.INT));
    //typeHash.put("BYTE",          new Integer(TypedObject.BYTE));
    //typeHash.put("TINYINT",       new Integer(TypedObject.BYTE));
    typeHash.put("LONG",          new Integer(TypedObject.LONG));
    typeHash.put("BIGINT",        new Integer(TypedObject.LONG));
    typeHash.put("SMALLINT",      new Integer(TypedObject.INT));
    typeHash.put("DOUBLE",        new Integer(TypedObject.DOUBLE));
    typeHash.put("FLOAT",         new Integer(TypedObject.FLOAT));
    typeHash.put("REAL",          new Integer(TypedObject.DOUBLE));
    typeHash.put("DATE",          new Integer(TypedObject.DATE));
    typeHash.put("DATETIME",      new Integer(TypedObject.TIMESTAMP));
    typeHash.put("TIMESTAMP",     new Integer(TypedObject.TIMESTAMP));
    typeHash.put("TIME",          new Integer(TypedObject.TIME));
    typeHash.put("BIT",           new Integer(TypedObject.BOOLEAN));
    typeHash.put("BOOLEAN",       new Integer(TypedObject.BOOLEAN));
    typeHash.put("BOOL",          new Integer(TypedObject.BOOLEAN));
    typeHash.put("INTERVALYEARTOMONTH",new Integer(TypedObject.YMINTERVAL));
    typeHash.put("INTERVALYEAR",new Integer(TypedObject.YMINTERVAL));
    typeHash.put("INTERVALMONTH",new Integer(TypedObject.YMINTERVAL));
    typeHash.put("INTERVALDAYTOSECOND",new Integer(TypedObject.YMINTERVAL));
    typeHash.put("INTERVALDAYTOMINUTE",new Integer(TypedObject.YMINTERVAL));
    typeHash.put("INTERVALDAYTOHOUR",new Integer(TypedObject.YMINTERVAL));
    typeHash.put("INTERVALSECOND",new Integer(TypedObject.YMINTERVAL));
    typeHash.put("INTERVALMINUTETOSECOND",new Integer(TypedObject.YMINTERVAL));
  }
*/
    ////////////////////////////////////////////////////////////// class variables
    private String name = null;
    private int type = -1;
    private int length = Integer.MAX_VALUE;
    private TypedObject defaultValue = null;
    private boolean nullable = true;
    private boolean primaryKey = false;
    private boolean unique = false;

    ///////////////////////////////////////////////////////////////// constructors
    //public Column(String name,String type) throws SQLException {
    //  this.name = name.toUpperCase();
    //  parseType(type);
    //}
    public Column(String name, int type) {
        this.name = name.toUpperCase();
        this.type = type;
    }

    ////////////////////////////////////////////////////////////////////// setters
    //public void setDefaultValue(String o) throws SQLException {
    //  if(o.startsWith("'") && o.endsWith("'")) o = o.substring(1,o.length()-1);
    //  defaultValue = DataFactory.createTypedObject(type,o);
    //}
    public void setDefaultValue(SQLTokenizer tz) throws SQLException {
        defaultValue = DataFactory.createTypedObject(type, tz);
    }

    public void setType(int type) { this.type = type; }

    public void setLength(int length) { this.length = length; }

    public void setNullable(boolean b) { nullable = b; }

    public void setPrimaryKey(boolean b) { primaryKey = b; }

    public void setUnique(boolean b) { unique = b; }

    ////////////////////////////////////////////////////////////////////// getters
    public String toString() {
        StringBuffer buf = new StringBuffer(name + " ");
        buf.append(DataFactory.prettyTypeName(type));
        if(length != -1) buf.append("(" + length + ")");
        if(!nullable) buf.append(" NOT NULL");
        if(primaryKey) buf.append(" PRIMARY KEY");
        if(unique) buf.append(" UNIQUE");
        if(defaultValue != null) buf.append(" DEFAULT '" + defaultValue + "'");
        return buf.toString();
    }

    public String getName() { return name; }

    public int getLength() { return length; }

    public int getType() { return type; }

    public Object getDefaultValue() { return defaultValue; }

    public boolean isNullable() { return nullable; }

    public boolean isUnique() { return unique; }

    public boolean isPrimaryKey() { return primaryKey; }

    //
    // deprecate this method
  /*public void parseType(String s) throws SQLException {
    if(s==null) throw new SQLException("No type specified for "+name);
    s = s.trim();
    if(s.length()==0) throw new SQLException("No type specified for "+name);
    //
    int pos = 0;
    int len = s.length();
    String lastToken = null;
    char[] upper = s.toUpperCase().toCharArray();
    char[] chars = s.toCharArray();
    while(pos<len) {
      while(s.charAt(pos)<33) pos++;
      String token = Tools.getNextToken(upper,false,pos);
      if(token.trim().length()==0) break;
      //
      if("DEFAULT".equals(token)) {
        pos += 7;
        while(s.charAt(pos)<33) pos++;
        String literal = Tools.getNextToken(chars,')',true,pos,chars.length);
        setDefaultValue(literal);
        pos += literal.length();
        lastToken = "DEFAULT";
      } else if("NOT".equals(token)) {
        lastToken = "NOT";
        pos+=3;
      } else if("PRIMARY".equals(token)) {
        lastToken = "PRIMARY";
        pos += 7;
      } else if("UNIQUE".equals(token)) {
        unique = true;
        pos += 6;
        lastToken = "UNIQUE";
      } else if("NULL".equals(token)) {
        if("NOT".equals(lastToken)) nullable = false;
        pos += 4;
      } else if("KEY".equals(token)) {
        if("PRIMARY".equals(lastToken)) {
          primaryKey = true;
          pos += 3;
        } else {
          throw new SQLException("Syntax error at "+token);
        }
      } else if("INTERVAL".equals(token)) {
        pos += 8;
        lastToken = "INTERVAL";
      } else if("YEAR".equals(token)) {
        if(!"INTERVAL".equals(lastToken)) throw new SQLException("Syntax error at "+token);
        type = TypedObject.YMINTERVAL;
        lastToken = "INTERVAL";
        pos += 4;
      } else if("MONTH".equals(token)) {
        if(!"INTERVAL".equals(lastToken)) throw new SQLException("Syntax error at "+token);
        type = TypedObject.YMINTERVAL;
        pos += 5;
      } else if("TO".equals(token)) {
        if(!"INTERVAL".equals(lastToken)) throw new SQLException("Syntax error at "+token);
        lastToken = "INTERVAL";
        pos += 2;
      } else {
        // must be a type
        //System.out.println("must be a type = "+token);
        pos += token.length();
        //String literal = Tools.getNextToken(chars,')',true,pos,chars.length);
        //System.out.println("literal="+literal);
        lastToken = "TYPENAME";
        if(token.indexOf('(')!=-1) {
          token = token.substring(0,token.indexOf('(')).trim();
        }
        Integer i = (Integer)typeHash.get(token);
        if(i==null) throw new SQLException("Syntax error at '"+token+"'");
        type = i.intValue();
      }
    }
  }*/
    public boolean equals(Column c) {
        if(!name.equals(c.getName())) return false;
        if(type != c.getType()) return false;
        if(length != c.getLength()) return false;
        if(defaultValue == null && c.getDefaultValue() != null) return false;
        if(defaultValue != null && c.getDefaultValue() == null) return false;
        if(defaultValue != null && !defaultValue.equals(c.getDefaultValue())) return false;
        if(nullable && !c.isNullable()) return false;
        if(!nullable && c.isNullable()) return false;
        if(unique && !c.isUnique()) return false;
        if(!unique && c.isUnique()) return false;
        if(primaryKey && !c.isPrimaryKey()) return false;
        if(!primaryKey && c.isPrimaryKey()) return false;
        return true;
    }
}