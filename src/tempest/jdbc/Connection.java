package tempest.jdbc;
/*
import java.sql.*;
import java.util.*;

public class Connection implements java.sql.Connection {
  private String databaseName=null;
  private String user=null;
  private String password=null;
  private boolean autoCommit=true;
  private boolean isClosed=false;
  private boolean readOnly=false;
  private String catalog=null;
  private int transactionIsolation=0;
  private SQLWarning warnings=null;
  private Map typeMap=null;
  private int holdability=0;
  private Savepoint savePoint=null;

  public Connection(String databaseName,String user,String password) {
    this.databaseName=databaseName;
    this.user=user;
    this.password=password;
  }
  public java.sql.Statement createStatement() {
    if(isClosed) return null;
    return new tempest.jdbc.Statement(this);
  }
  public java.sql.Statement createStatement(int type,int concurrency) {
    return null;
  }
  public java.sql.Statement createStatement(int a,int b,int c) {
    return null;
  }
  public void releaseSavepoint(Savepoint sp) {

  }
  public void rollback() {

  }
  public void rollback(Savepoint sp) {

  }
  public Savepoint setSavepoint() {
    return savePoint;
  }
  public Savepoint setSavepoint(String s) {
    return savePoint;
  }
  public int getHoldability() {
    return holdability;
  }
  public void setHoldability(int i) {
    holdability=i;
  }
  public void setTypeMap(Map map) {
    typeMap=map;
  }
  public Map getTypeMap() {
    return typeMap;
  }
  public void clearWarnings() {
    warnings=null;
  }
  public SQLWarning getWarnings() {
    return warnings;
  }
  public int getTransactionIsolation() {
    return transactionIsolation;
  }
  public void setTransactionIsolation(int i) {
    transactionIsolation=i;
  }
  public String getCatalog() {
    return catalog;
  }
  public void setCatalog(String s) {
    catalog=s;
  }
  public boolean isReadOnly() {
    return readOnly;
  }
  public void setReadOnly(boolean b) {
    readOnly=b;
  }
  public boolean isClosed() {
    return isClosed;
  }
  public void close() {
    isClosed=true;
  }
  public void commit() {

  }
  public boolean getAutoCommit() {
    return autoCommit;
  }
  public void setAutoCommit(boolean b) {
    autoCommit=b;
  }
  public String nativeSQL(String s) {
    return s;
  }
  public String getDatabaseName() {
    return databaseName;
  }
  public String toString() {
    return "tempest.jdbc.Connection(database="+databaseName+",autocommit="+autoCommit+",closed="+isClosed+")";
  }
  ////////////////////////////////////////////////////////////////////////////// not yet implemented
  public java.sql.DatabaseMetaData getMetaData() {
   return null;
  }
  public PreparedStatement prepareStatement(String s) {
    return null;
  }
  public PreparedStatement prepareStatement(String s,int[] str) {
    return null;
  }
  public PreparedStatement prepareStatement(String s,String[] str) {
    return null;
  }
  public PreparedStatement prepareStatement(String s,int str) {
    return null;
  }
  public PreparedStatement prepareStatement(String s,int a,int b) {
    return null;
  }
  public PreparedStatement prepareStatement(String s,int a,int b,int c) {
    return null;
  }
  public CallableStatement prepareCall(String s) {
   return null;
  }
  public CallableStatement prepareCall(String s,int a,int b) {
    return null;
  }
  public CallableStatement prepareCall(String s,int a,int b ,int c) {
    return null;
  }
  //////////////////////////////////////////////////////////////////////////////
  public java.sql.ResultSet execute(String query) throws SQLException {
    return null;
  }
 }*/
