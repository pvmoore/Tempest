package tempest.test;

public class TestHarness {
  /*
  private ArrayList tests       = new ArrayList();
  private Session session       = null;
  private Table table           = null;
  private Table previousResult  = null;
  private StringBuffer buffer   = new StringBuffer();
  private boolean expectSuccess = true;
  private boolean expectException = false;

  public static void main(String[] args) { new TestHarness(); }
  public TestHarness() {
    try{
      Engine.start("data");
      log("Starting...");
      //String ss = "  ";
      //SQLTokenizer t = new SQLTokenizer(ss);
      //System.out.println(ss);
      //System.out.println(""+t);
      //
      session   = new Session();
      Database database = Engine.createDatabase("test","",Database.MEMORY);
      if(!database.open()) throw new SQLException("Couldn't open database");
      Catalog catalog   = database.getCatalog(null);
      Schema schema     = catalog.getSchema(null);
      session.setDatabase(database);
      session.setCatalog(catalog);
      session.setSchema(schema);
      //
      File start = new File("test");
      recurseTests(start);
      log("Found "+tests.size()+" test file"+(tests.size()==1?"":"s"));
      for(int i=0;i<tests.size();i++) {
        runTest((File)tests.get(i));
      }
    }catch(Exception e) {
      log("error: "+e);
      e.printStackTrace();
    }finally{
      log("Finished");
      Engine.destroy();
    }
  }
  private void log(String s) {
    Engine.getLogger().log("TestHarness: "+s);
  }
  private void recurseTests(File f) {
    if(f.isDirectory()) {
      File[] ff = f.listFiles();
      for(int i=0;i<ff.length;i++) { recurseTests(ff[i]); }
    } else {
      if(f.getName().endsWith(".test")) {
        tests.add(f);
      }
    }
  }
  private String getBetween(String s,char c) {
    return getBetween(s,c,c,0);
  }
  private String getBetween(String s,char c1,char c2) {
    return getBetween(s,c1,c2,0);
  }
  private String getBetween(String s,char c1,char c2,int start) {
    if(s==null) return "";
    int pos  = s.indexOf(c1,start);
    if(pos==-1) return "";
    int pos2 = s.indexOf(c2,pos+1);
    if(pos2==-1) return "";
    return s.substring(pos+1,pos2);
  }
  private String[] readFile(File f) {
    FileReader fr = null;
    BufferedReader br = null;
    try{
      fr = new FileReader(f);
      br = new BufferedReader(fr);
      String line = null;
      ArrayList lines = new ArrayList();
      while((line=br.readLine())!=null) {
        line = line.trim();
        lines.add(line);
      }
      return (String[])lines.toArray(new String[0]);
    }catch(Exception e) {
      log("readFile: "+e);
    }finally{
      try{ if(br!=null) br.close(); }catch(Exception ee) {}
      try{ if(fr!=null) fr.close(); }catch(Exception ee) {}
    }
    return new String[0];
  }
  private void runTest(File f) throws Exception {
    log("Running test "+f);
    String[] lines = readFile(f);
    log("File contains "+lines.length+" lines");
    for(int i=0;i<lines.length;i++) {
      parseLine(lines[i]);
    }
  }
  private void parseROW(String s) throws Exception {
    int pos = 0;
    boolean insideQuote = false;
    char[] chars = s.toCharArray();
    ArrayList cols = new ArrayList();
    for(int i=0;i<chars.length;i++) {
      if(chars[i]=='\'') {
        insideQuote = !insideQuote;
      }
      if(chars[i]==',' && !insideQuote) {
        cols.add(new String(chars,0,pos));
        pos = 0;
      }
      if(insideQuote && !(pos==0 && chars[i]=='\'')) {
        chars[pos++] = chars[i];
      }
    }
    if(pos>0) {
      cols.add(new String(chars,0,pos));
    }
    //if(table==null) {
    //  table = session.getSchema().createTable("temp_check_table");
    //  for(int i=0;i<cols.size();i++) {
    //    String name = (String)cols.get(i);
    //    String type = getBetween(name,'(',')');
    //    name = name.substring(0,name.indexOf('('));
    //    int intType = DataFactory.parseType(type);
    //    Column col = new Column(name,intType);
    //    table.addColumn(col);
    //  }
    //} else {
      for(int i=0;i<cols.size();i++) {
        String col = (String)cols.get(i);
        cols.set(i,DataFactory.createTypedObject(table.getColumn(i).getType(),col));
      }
      TypedObject[] objs = (TypedObject[])cols.toArray(new TypedObject[0]);
      _ROW row = new _ROW(table,objs);
      table.addRow(row,session);
    //}
  }
  private void parseColumn(String s) throws Exception {
    if(table==null) table = session.getSchema().createTable("temp_check_table");
    String name = "";
    String type = "";
    int pos = -1;
    if((pos=s.indexOf("name="))!=-1) name = getBetween(s,'\'','\'',pos);
    if((pos=s.indexOf("type="))!=-1) type = getBetween(s,'\'','\'',pos);
    Column col = new Column(name,type);
    if(s.indexOf("pk='true'")!=-1) col.setPrimaryKey(true);
    if(s.indexOf("unique='true'")!=-1) col.setUnique(true);
    if(s.indexOf("nullable='false'")!=-1) col.setNullable(false);
    if((pos=s.indexOf("default="))!=-1) col.setDefaultValue(getBetween(s,'\'','\'',pos));
    table.addColumn(col);
    //log("col="+col);
  }
  private void parseLine(String line) throws Exception {
    if(line.startsWith("--")) {
      // comment
    } else if(line.startsWith("#note")) {
      log(getBetween(line,'\''));
    } else if(line.startsWith("#include")){
      String[] lines = readFile(new File("test/"+getBetween(line,'\'')));
      for(int i=0;i<lines.length;i++) parseLine(lines[i]);
    } else if(line.startsWith("#check query")) {
      expectSuccess = true;
      if(line.indexOf("expect fail")!=-1) expectSuccess = false;
      boolean result = table.equals(previousResult);
      if(expectSuccess) {
        if(result) {
          log("Test passed");
        } else {
          log("table="+table);
          log("previous="+previousResult);
          throw new Exception("Test failed (not equal)");
        }
      } else {
        if(result) {
          throw new Exception("Test failed (equal)");
        } else {
          log("Test failed as expected");
        }
      }
      table = null;
    } else if(line.startsWith("#expect exception")) {
      expectException = true;
    } else if(line.startsWith("#check update")) {
      // todo
    } else if(line.startsWith("\'") && buffer.length()==0) {
      parseROW(line);
    } else if(line.startsWith("#column")) {
      parseColumn(line);
    } else {
      if(line.endsWith(";")) {
        line = line.substring(0,line.length()-1);
        try{
          SQL sql = SQL.getSQL(session,buffer.toString()+line);
          previousResult = sql.execute();
        }catch(Exception e) {
          if(!expectException) throw e;
          log("Test caused exception as expected");
          log("Exception was : "+e);
        }
        expectException = false;
        buffer.setLength(0);
      } else {
        buffer.append(line);
      }
    }
  }

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
}
