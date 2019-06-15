package tempest;

//import tempest.*;
//import tempest.data.*;
//import tempest.interfaces.*;
//import java.util.*;
//import java.sql.*;
//import java.io.*;

public class Tools {
    /**
     * static utilities
     */

    public static String removeLeadingZeros(String s) {
        if(s == null) return "";
        int pos = 0;
        int len = s.length();
        while(pos < len && s.charAt(pos) == '0') pos++;
        if(pos > 0) return s.substring(pos);
        return s;
    }

    public static int parseInt(String s, int d) {
        try {
            return Integer.parseInt(s);
        } catch(Exception e) {}
        return d;
    }
  
  /*public static String[] parseLine(String line) {
    if(line==null) return new String[0];
    StringTokenizer tokens=new StringTokenizer(line);
    if(tokens==null || tokens.countTokens()==0) return new String[0];
    String[] s=new String[tokens.countTokens()];
    for(int i=0;i<s.length;i++) {
      s[i]=tokens.nextToken();
    }
    return s;
  }*/
  /*public static String getPreviousToken(String s,boolean quoteAware) {
    if(s==null || s.length()==0) return null;
    return getPreviousToken(s,quoteAware,s.length()-1);
  }*/
  /*public static String getPreviousToken(String s,boolean quoteAware,int start) {
    if(s==null || start>=s.length()) return null;
    s=s.substring(0,start+1);
    char[] chars=s.toCharArray();
    return getPreviousToken(chars,quoteAware,start);
  }
  public static String getPreviousToken(char[] chars,boolean quoteAware,int start) {
    System.out.println("len="+chars.length+" start="+start);
    if(chars==null || chars.length<start) return null;
    int i=start;
    boolean inQuote=false;
    for(;i>=0;i--) {
      char c=chars[i];
      System.out.println("c="+c);
      if(quoteAware) {
        if(c=='\'') inQuote=!inQuote;
        if(!inQuote && c<33) break;
      } else {
        if(c<33) break;
      }
    }
    if(i<0) i=0;
    return new String(chars,i,start-i);
  }
  public static String getNextToken(String s,boolean quoteAware) {
    return getNextToken(s,quoteAware,0);
  }
  
  public static String getNextToken(String s,boolean quoteAware,int start) {
    if(s==null) return null;
    if(start>0) s=s.substring(start);
    char[] chars=s.toCharArray();
    return getNextToken(chars,quoteAware,0);
  }
  public static String getNextToken(char[] chars,boolean quoteAware,int start) {
    if(chars==null || chars.length<=start) return null;
    int i=start;
    boolean inQuote=false;
    for(i=start;i<chars.length;i++) {
      char c=chars[i];
      if(quoteAware) {
        if(c=='\'') inQuote = !inQuote;
        if(!inQuote && c<33) break;
      } else {
        if(c<33) break;
      }
    }
    return new String(chars,start,i-start);
  }
  public static String getNextToken(String s,char delim,boolean quoteAware) {
    return getNextToken(s,delim,quoteAware,0);
  }
  public static String getNextToken(String s,char delim,boolean quoteAware,int start) {
    if(s==null) return null;
    if(start>0) s=s.substring(start);
    char[] chars=s.toCharArray();
    return getNextToken(chars,delim,quoteAware,0,chars.length);
  }
  public static String getNextToken(char[] chars,char delim,boolean quoteAware,int start,int end) {
    if(chars==null || chars.length<=start) return null;
    if(end>chars.length) end=chars.length;
    int i=start;
    boolean inQuote=false;
    for(i=start;i<end;i++) {
      char c=chars[i];
      if(quoteAware) {
        if(c=='\'') inQuote=!inQuote;
        if(!inQuote && c==delim) break;
      } else {
        if(c==delim) break;
      }
    }
    return new String(chars,start,i-start);
  }
  */
  /*public static int findChar(char[] chars,char ch,int start) {
    if(chars==null || chars.length<start) return -1;
    boolean inQuote=false;
    for(int i=start;i<chars.length;i++) {
      char c=chars[i];
      if(c=='\'') inQuote=!inQuote;
      if(!inQuote && c==ch) return i;
    }
    return -1;
  }*/
  /*public static String getFullPath(String p) {
    if(p.startsWith("/") || p.startsWith("\\")) return p;
    String root=Engine.getRootPath();
    if(!root.endsWith("/") && !root.endsWith("\\")) root+=File.separator;
    return root+p;
  }*/
    /**
     * numeric characters or '.' are ok
     */
    //public static boolean isString(String s) {
    //  if(s==null) return false;
    //  s = s.trim();
    //  if(s.startsWith("'") && s.endsWith("'")) {
    //    String woq = Tools.stripQuoted(s);
    //    if(woq.trim().length()==0) return true;
    //  }
    //  return false;
    //}
    /**
     * only does STRING,LONG,DOUBLE,YMINTERVAL,DTINTERVAL at the moment
     */
  /*public static int parseType(String s) {
    if(s==null) return -1;
    s = s.trim();
    //if(s.startsWith("'")) {
    //  if(isString(s)) return TypedObject.STRING;
    //  return -1;
    //}
    if(s.startsWith("'") && s.endsWith("'")) return TypedObject.STRING;
    String lower = s.toLowerCase();
    if(lower.startsWith("interval")) {
      if(lower.indexOf("year")!=-1 || lower.indexOf("month")!=-1) {
        return TypedObject.YMINTERVAL;
      }
      return TypedObject.DTINTERVAL;
    }
    if(isNumber(s)) {
      if(s.indexOf('.')!=-1) return TypedObject.DOUBLE;
      return TypedObject.LONG;
    }
    return -1;
  }*/
  /*public static int getReturnType(int a,int b) {
    // assume types are compatible
    if(a==b) return a;
    return Math.max(a,b);
  }*/
  /*public static String formatQuotes(String s) {
    if(s==null) return "";
    s=s.trim();
    if(s.charAt(0)=='\'') s=s.substring(1);
    if(s.charAt(s.length()-1)=='\'') s=s.substring(0,s.length()-1);
    int pos=0;
    while((pos=s.indexOf("''"))!=-1) {
      s=s.substring(0,pos)+s.substring(pos+1);
    }
    return s;
  }*/
  /*public static String stripQuoted(String s) {
    char[] chars=s.toCharArray();
    int len=chars.length;
    boolean inQuote=false;
    for(int i=0;i<len;i++) {
      char c=chars[i];
      if(inQuote || c=='\'') chars[i]=' ';
      if(c=='\'') {
        inQuote=!inQuote;
      }
    }
    return new String(chars,0,len);
  }*/
  /*public static String getBetween(String s,char lc,char rc) {
    char[] chars = s.toCharArray();
    int pos   = 0;
    boolean started = false;
    for(int i=0;i<chars.length;i++) {
      char c = chars[i];
      if(c==rc) break;
      if(started) chars[pos++] = c;
      if(c==lc) started = true;
    }
    return new String(chars,0,pos);
  }*/
  /*public static String stripInsideBrackets(String s) {
    char[] chars=s.toCharArray();
    int len=chars.length;
    int depth=0;
    for(int i=0;i<len;i++) {
      char c=chars[i];
      if(c=='(') depth++;
      if(depth>0) chars[i]=' ';
      if(c==')') depth--;
    }
    return new String(chars,0,len);
  }*/
  /*public static String removeWhitespace(String s) {
    if(s==null) return "";
    char[] chars = s.toCharArray();
    int pos = 0;
    for(int i=0;i<chars.length;i++) {
      if(chars[i]>32) chars[pos++] = chars[i];
    }
    return new String(chars,0,pos);
  }*/
  /*public static boolean isNumber(String s) {
    if(s==null) return false;
    char[] chars=s.toCharArray();
    for(int i=0;i<chars.length;i++) {
      char c=chars[i];
      if((c<'0' || c>'9') && c!='.') return false;
    }
    return true;
  }*/
  /*
  public static int[] findCharPositions(String s,char cc) {
    IntArray via=new IntArray();
    if(s==null) return new int[0];
    char[] chars=s.toCharArray();
    for(int i=0;i<chars.length;i++) {
      char c=chars[i];
      if(c==cc) via.add(i);
    }
    return via.toArray();
  }*/
  /*
  public static String[] tokenize(String s,int delim) {
    if(s==null) return new String[0];
    char[] chars = s.toCharArray();
    ArrayList al = new ArrayList();
    int pos = 0;
    for(int i=0;i<chars.length;i++) {
      char c = chars[i];
      if(c==delim) {
        al.add(new String(chars,0,pos));
        pos = 0;
      } else {
        chars[pos++] = c;
      }
    }
    al.add(new String(chars,0,pos));
    return (String[])al.toArray(new String[0]);
  }*/
}