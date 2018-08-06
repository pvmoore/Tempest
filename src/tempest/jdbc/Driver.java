package tempest.jdbc;
/*
import java.sql.*;
import java.util.*;

public class Driver implements java.sql.Driver {
  public static final int MAJOR_VERSION = 1;
  public static final int MINOR_VERSION = 4;

  static {
    try {
      DriverManager.registerDriver(new tempest.jdbc.Driver());
    } catch(Exception e) {
    }
  }

  public Driver() {}
  public boolean jdbcCompliant() { return false; }
  public int getMinorVersion() { return MINOR_VERSION; }
  public int getMajorVersion() { return MAJOR_VERSION; }
  public DriverPropertyInfo[] getPropertyInfo(String url,Properties info) {
    DriverPropertyInfo pinfo[]=new DriverPropertyInfo[2];
    DriverPropertyInfo p=new DriverPropertyInfo("user",null);
    p.value=info.getProperty("user");
    p.required=true;
    pinfo[0]=p;
    p=new DriverPropertyInfo("password",null);
    p.value=info.getProperty("password");
    p.required=true;
    pinfo[1]=p;
    return pinfo;
  }
  public boolean acceptsURL(String url) {
    if(url!=null && url.toLowerCase().indexOf("jdbc:tempest:")==0) {
      return true;
    }
    return false;
  }
  public java.sql.Connection connect(String url,Properties props) {
    if(!acceptsURL(url)) return null;
    String user=props.getProperty("user");
    String password=props.getProperty("password");
    String database=url.substring(url.lastIndexOf(':')+1);
    return new tempest.jdbc.Connection(database,user,password);
  }
 }*/
