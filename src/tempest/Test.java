package tempest;
/*
import tempest.jdbc.*;
import tempest.types.*;
import tempest.sql.*;
import java.sql.*;
import java.util.*;

public class Test {
  public static void main(String[] args) { new Test(); }
  public void log(String s) { System.out.println(s); }
  public Test() {
    log("Test suite starting");
    log("Starting Tempest Engine");
    tempest.Engine.start("/work/Tempest/data/");
    //
    java.sql.Connection con=null;
    java.sql.Statement stmt=null;
    java.sql.ResultSet rs=null;
    try{
      //memTest();
      //Class.forName("tempest.jdbc.Driver");
      //con=DriverManager.getConnection("jdbc:tempest:test","tempest","tempest");
      //log("con="+con);
      //stmt=con.createStatement();
      //log("stmt="+stmt);
      //
      // ignore jdbc for the moment
      // do everything manually
      Session session   = new Session();
      Database database = Database.getDatabase("test","",Database.MEMORY);
      if(!database.open()) throw new SQLException("Couldn't open database");
      session.setDatabase(database);
      //
      SQL sql0=SQL.getSQL(session,"create table test (ref autonumber not null primary  key unique, title varchar(255) default 'he'',llo',  num  int)");
      SQL sql1=SQL.getSQL(session,"insert into test values ('pants',4,32.4,'yellow')");
      SQL sql2=SQL.getSQL(session,"insert test (title,num) values ('blah',33)");
      SQL sql3=SQL.getSQL(session,"SELECT 'tom''s WHERE clause',10,'hello',title FROM "+
                         "test t,pants p where ((title='yop' or (title='yip')) AND num=2)"+
                         " GROUP by title "+
                         " having blobby  "+
                         " order  by title"+
                         "");
      //
      Table testTable=database.getTable("TEST");
      testTable.displayRowContents();
    }catch(Exception e) {
      log("Exception : "+e);
    }finally{
      try{ if(rs!=null)   rs.close();   }catch(Exception ee) {}
      try{ if(stmt!=null) stmt.close(); }catch(Exception ee) {}
      try{ if(con!=null)  con.close();  }catch(Exception ee) {}
    }
    //
    tempest.Engine.destroy();
    log("Test suite complete");
  }
  private void memTest() {
    System.gc();
    Runtime r=Runtime.getRuntime();
    long memUsed = r.totalMemory() - r.freeMemory();
    System.out.println("mem used = "+memUsed);
    int count=1000000;
    Object[] obs=new Object[count];
    long start=System.currentTimeMillis();
    for(int i=0;i<count;i++) {
      //obs[i]=new Long(i);
      //obs[i]=new _long(i);
    }
    java.util.Arrays.sort(obs);
    long end=System.currentTimeMillis();
    long memUsed2 = r.totalMemory() - r.freeMemory();
    System.out.println("mem used is now = "+memUsed2);
    System.out.println("average = "+((memUsed2-memUsed)/count)+" bytes per object");
    System.out.println("speed was "+(end-start)+" millis");
  }
}
*/