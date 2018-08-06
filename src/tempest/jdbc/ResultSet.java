package tempest.jdbc;
/*
import tempest.*;
import java.sql.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.math.*;

public class ResultSet implements java.sql.ResultSet {
  private tempest.jdbc.Statement stmt=null;
  private int currentRow=-1;
  private int fetchSize=0;
  private String cursorName=null;
  private SQLWarning warnings=null;
  private ResultRow[] results=null;

  public ResultSet(tempest.jdbc.Statement stmt,ResultRow[] results) {
    this.stmt=stmt;
    this.results=results;
  }

  public java.sql.Statement getStatement() {
    return stmt;
  }
  public void moveToCurrentRow() {

  }
  public void moveToInsertRow() {

  }
  public void cancelRowUpdates() {

  }
  public void refreshRow() {

  }
  public int getConcurrency() {
    return java.sql.ResultSet.CONCUR_READ_ONLY;
  }
  public int getType() {
    return java.sql.ResultSet.TYPE_FORWARD_ONLY;
  }
  public int getFetchSize() {
    return fetchSize;
  }
  public void setFetchSize(int i) {
    fetchSize=i;
  }
  public int getFetchDirection() {
    return java.sql.ResultSet.FETCH_FORWARD;
  }
  public void setFetchDirection(int i) {

  }
  public int findColumn(String s) {
    return 0;
  }
  public java.sql.ResultSetMetaData getMetaData() {
    return null;
  }
  public String getCursorName() {
    return cursorName;
  }
  public void clearWarnings() {
    warnings=null;
  }
  public SQLWarning getWarnings() {
    return warnings;
  }
  public void close() {

  }
  //////////////////////////////////////////////////////////////////////////////
  public boolean isLast() {
    return false;
  }
  public boolean isFirst() {
    return false;
  }
  public boolean isAfterLast() {
    return false;
  }
  public boolean isBeforeFirst() {
    return false;
  }
  public boolean wasNull() {
    return false;
  }
  ////////////////////////////////////////////////////////////////////////////// move pointer
  public boolean next() {
    return false;
  }
  public boolean previous() {
    return false;
  }
  public boolean relative(int i) {
    return false;
  }
  public boolean absolute(int i) {
    return false;
  }
  public int getRow() {
    return currentRow;
  }
  public boolean last() {
    return false;
  }
  public void beforeFirst() {

  }
  public boolean first() {
    return false;
  }
  public void afterLast() {

  }
  ////////////////////////////////////////////////////////////////////////////// updates
  public void deleteRow() {

  }
  public boolean rowDeleted() {
    return false;
  }
  public void updateRow() {

  }
  public boolean rowUpdated() {
    return false;
  }
  public void insertRow() {

  }
  public boolean rowInserted() {
    return false;
  }
  public void updateArray(int i,Array a) {

  }
  public void updateArray(String s,Array a) {

  }
  public void updateClob(int i,Clob c) {

  }
  public void updateClob(String s,Clob c) {

  }
  public void updateBlob(int i,Blob b) {

  }
  public void updateBlob(String s,Blob b) {

  }
  public void updateRef(int i,Ref r) {

  }
  public void updateRef(String s,Ref r) {

  }
  public void updateObject(int i,Object d) {

  }
  public void updateObject(int i,Object d,int ii) {

  }
  public void updateObject(String s,Object o) {

  }
  public void updateObject(String s,Object o,int i) {

  }
  public void updateCharacterStream(int i,Reader r,int ii) {

  }
  public void updateCharacterStream(String s,Reader r,int i) {

  }
  public void updateBinaryStream(int ii,InputStream r,int i) {

  }
  public void updateBinaryStream(String s,InputStream r,int i) {

  }
  public void updateAsciiStream(int ii,InputStream r,int i) {

  }
  public void updateAsciiStream(String s,InputStream r,int i) {

  }
  public void updateTimestamp(int i,Timestamp t) {

  }
  public void updateTimestamp(String s,Timestamp t) {

  }
  public void updateTime(int i,Time t) {

  }
  public void updateTime(String s,Time t) {

  }
  public void updateDate(int i,java.sql.Date t) {

  }
  public void updateDate(String s,java.sql.Date t) {

  }
  public void updateBytes(int i,byte[] bytes) {

  }
  public void updateBytes(String s,byte[] bytes) {

  }
  public void updateString(int i,String ss) {

  }
  public void updateString(String s,String ss) {

  }
  public void updateBigDecimal(int i,BigDecimal bd) {

  }
  public void updateBigDecimal(String s,BigDecimal bd) {

  }
  public void updateDouble(int i,double d) {

  }
  public void updateDouble(String s,double d) {

  }
  public void updateFloat(int i,float d) {

  }
  public void updateFloat(String s,float d) {

  }
  public void updateLong(int i,long d) {

  }
  public void updateLong(String s,long d) {

  }
  public void updateInt(int i,int d) {

  }
  public void updateInt(String s,int d) {

  }
  public void updateShort(int i,short d) {

  }
  public void updateShort(String s,short d) {

  }
  public void updateByte(int i,byte d) {

  }
  public void updateByte(String s,byte d) {

  }
  public void updateBoolean(int i,boolean d) {

  }
  public void updateBoolean(String s,boolean d) {

  }
  public void updateNull(int i) {

  }
  public void updateNull(String s) {

  }
  ////////////////////////////////////////////////////////////////////////////// get data types
  public URL getURL(int i) {
    return null;
  }
  public URL getURL(String s) {
    return null;
  }
  public Timestamp getTimestamp(int i,Calendar c) {
    return null;
  }
  public Timestamp getTimestamp(String s,Calendar c) {
    return null;
  }
  public Time getTime(int i,Calendar c) {
    return null;
  }
  public Time getTime(String s,Calendar c) {
    return null;
  }
  public java.sql.Date getDate(int i,Calendar c) {
    return null;
  }
  public java.sql.Date getDate(String s,Calendar c) {
    return null;
  }
  public Array getArray(int i) {
    return null;
  }
  public Array getArray(String s) {
    return null;
  }
  public Clob getClob(int i) {
    return null;
  }
  public Clob getClob(String s) {
    return null;
  }
  public Blob getBlob(int i) {
    return null;
  }
  public Blob getBlob(String s) {
    return null;
  }
  public Ref getRef(int i) {
    return null;
  }
  public Ref getRef(String s) {
    return null;
  }
  public Object getObject(int i,Map map) {
    return null;
  }
  public Object getObject(String s,Map map) {
    return null;
  }
  public BigDecimal getBigDecimal(int i) {
    return null;
  }
  public BigDecimal getBigDecimal(String s) {
    return null;
  }
  public BigDecimal getBigDecimal(int column,int scale) {
    return null;
  }
  public BigDecimal getBigDecimal(String s,int scale) {
    return null;
  }
  public Reader getCharacterStream(int i) {
    return null;
  }
  public Reader getCharacterStream(String s) {
    return null;
  }
  public Object getObject(int i) {
    return null;
  }
  public Object getObject(String s) {
    return null;
  }
  public InputStream getBinaryStream(int i) {
    return null;
  }
  public InputStream getBinaryStream(String s) {
    return null;
  }
  public InputStream getUnicodeStream(int i) {
    return null;
  }
  public InputStream getUnicodeStream(String s) {
    return null;
  }
  public InputStream getAsciiStream(int i) {
    return null;
  }
  public InputStream getAsciiStream(String s) {
    return null;
  }
  public Timestamp getTimestamp(int i) {
    return null;
  }
  public Timestamp getTimestamp(String s) {
    return null;
  }
  public Time getTime(int i) {
    return null;
  }
  public Time getTime(String s) {
    return null;
  }
  public java.sql.Date getDate(int i) {
    return null;
  }
  public java.sql.Date getDate(String s) {
    return null;
  }
  public byte[] getBytes(int i) {
    return null;
  }
  public byte[] getBytes(String s) {
    return null;
  }
  public double getDouble(int i) {
    return 0.0;
  }
  public double getDouble(String s) {
    return 0.0;
  }
  public float getFloat(int i) {
    return 0.0f;
  }
  public float getFloat(String s) {
    return 0.0f;
  }
  public long getLong(int i) {
    return 0;
  }
  public long getLong(String s) {
    return 0;
  }
  public int getInt(int i) {
    return 0;
  }
  public int getInt(String s) {
    return 0;
  }
  public short getShort(int i) {
    return 0;
  }
  public short getShort(String s) {
    return 0;
  }
  public byte getByte(int i) {
    return 0;
  }
  public byte getByte(String s) {
    return 0;
  }
  public boolean getBoolean(int i) {
    return false;
  }
  public boolean getBoolean(String s) {
    return false;
  }
  public String getString(int i) {
    return null;
  }
  public String getString(String s) {
    return null;
  }
 }*/
