package tempest.sql;

import tempest.collections.IntList;
import tempest.collections.StringList;

import java.io.CharArrayWriter;
import java.util.ArrayList;

public class SQLTokenizer {

    private IntList types = new IntList();
    private StringList tokens = new StringList();
    private CharArrayWriter token = new CharArrayWriter();
    private int first = 0;
    private int last = -1;

    public SQLTokenizer() {
    }

    public SQLTokenizer(SQLTokenizer tz, int start, int end) {
        this.tokens = tz.tokens;
        this.types = tz.types;
        this.first = start;
        this.last = end;
    }

    public SQLTokenizer(String sql) {
        char[] chars = sql.toCharArray();
        boolean quote = false;
        int len = chars.length;
        int intervalPos = 0;
        //
        loop:
        for(int i = 0; i < len; i++) {
            char c = chars[i];
            //
            if(c == '\'') {
                quote = !quote;
                if(!quote) {
                    if(i < len - 1 && chars[i + 1] == '\'') continue loop;
                    token.write(c);
                    writeToken(STRINGLITERAL);
                    continue loop;
                }
                token.write(c);
                continue loop;
            }
            //
            if(!quote) {
                switch(c) {
                    //case '\'' : quote = true; writeToken(TEXT); continue loop;
                    case ',':
                        writeToken(TEXT);
                        token.write(c);
                        writeToken(COMMA);
                        continue loop;
                    case ':':
                        writeToken(TEXT);
                        token.write(c);
                        writeToken(COLON);
                        continue loop;
                    case ';':
                        writeToken(TEXT);
                        token.write(c);
                        writeToken(SEMICOLON);
                        continue loop;
                    case '(':
                        writeToken(TEXT);
                        token.write(c);
                        writeToken(LEFTBRACKET);
                        continue loop;
                    case ')':
                        writeToken(TEXT);
                        token.write(c);
                        writeToken(RIGHTBRACKET);
                        continue loop;
                    case '=':
                        if(i > 0) {
                            if(chars[i - 1] == '>') {
                                token.write(c);
                                writeToken(GTEQ);
                                continue loop;
                            }
                            if(chars[i - 1] == '<') {
                                token.write(c);
                                writeToken(LTEQ);
                                continue loop;
                            }
                        }
                        writeToken(TEXT);
                        token.write(c);
                        writeToken(EQ);
                        continue loop;
                    case '+':
                        writeToken(TEXT);
                        token.write(c);
                        writeToken(ADD);
                        continue loop;
                    case '-':
                        writeToken(TEXT);
                        token.write(c);
                        writeToken(SUB);
                        continue loop;
                    case '/':
                        writeToken(TEXT);
                        token.write(c);
                        writeToken(DIV);
                        continue loop;
                    case '%':
                        writeToken(TEXT);
                        token.write(c);
                        writeToken(MOD);
                        continue loop;
                    case '!':
                        if(i < len - 1 && chars[i + 1] != '=') {
                            writeToken(TEXT);
                            token.write(c);
                            writeToken(NOTEQ);
                            continue loop;
                        }
                        writeToken(TEXT);
                    case '<':
                        if(i < len - 1 && chars[i + 1] != '=' && chars[i + 1] != '>') {
                            writeToken(TEXT);
                            token.write(c);
                            writeToken(LT);
                            continue loop;
                        }
                        writeToken(TEXT);
                    case '>':
                        if(i > 0 && chars[i - 1] == '<') {
                            token.write(c);
                            writeToken(NOTEQ);
                            continue loop;
                        }
                        if(i == len - 1 || chars[i + 1] != '=') {
                            writeToken(TEXT);
                            token.write(c);
                            writeToken(GT);
                            continue loop;
                        }
                        writeToken(TEXT);
                    case '|':
                        if(i > 0 && chars[i - 1] == '|') {
                            token.write(c);
                            writeToken(CONCAT);
                            continue loop;
                        }
                        break;
                    case '*': // be aware of table.* or alias.* or count(*)
                        writeToken(TEXT);
                        token.write(c);
                        writeToken(MUL);
                        continue loop;
                }
                if(c < 33) {
                    if(token.size() > 0) writeToken(TEXT);
                } else {
                    token.write(c);
                }
            } else {
                token.write(c);
            }
        }
        writeToken(TEXT);
        //
        last = tokens.size() - 1;
    }

    private void writeToken(int type) {
        if(token.size() == 0) return;
        String tokenStr = token.toString();
        if(type == TEXT) {
            if(tokenStr.indexOf('.') != -1) type = IDENTIFIER;
        }
        switch(type) {
            case IDENTIFIER:
                if(!tokenStr.startsWith("\"")) {
                    tokenStr = tokenStr.toUpperCase();
                }
                break;
            case KEYWORD:
            case TEXT:
                // check if this is a *special* keyword
                tokenStr = tokenStr.toUpperCase();
                int nn = Keyword.getInt(tokenStr);//(Integer)keywords.get(tokenStr);
                if(nn != -1) type = nn;
        }
        tokens.add(tokenStr);
        types.add(type);
        token.reset();
    }

    public String toString() {
        StringBuilder buf = new StringBuilder("" + (last - first + 1) + " TOKENS [");
        int n = 0;
        for(int i = first; i <= last; i++) {
            buf.append("\n").append(n++).append(" ").append(Keyword.getString(types.get(i)));
            buf.append("\t").append(tokens.get(i));
        }
        buf.append("\n]");
        return buf.toString();
    }

    //
    public int indexOf(int type) { return indexOf(type, 0); }

    public int indexOf(int type, int start) {
        for(int i = start + first; i <= last; i++) {
            if(types.get(i) == type) return i - first;
        }
        return -1;
    }

    public int indexOfOB(int type) { // Outside brackets
        return indexOfOB(type, 0);
    }

    public int indexOfOB(int type, int start) { // Outside brackets
        int depth = 0;
        for(int i = first; i <= last; i++) {
            if(types.get(i) == LEFTBRACKET) {
                depth++;
            } else if(types.get(i) == RIGHTBRACKET) {
                depth--;
            }
            if(depth == 0 && types.get(i) == type) {
                return i - first;
            }
        }
        return -1;
    }

    public int lastIndexOf(int type) {
        return lastIndexOf(type, last - first);
    }

    public int lastIndexOf(int type, int end) {
        for(int i = end + first; i >= first; i--) {
            if(types.get(i) == type) return i - first;
        }
        return -1;
    }

    public int indexOf(String s) { return indexOf(s, 0); }

    public int indexOf(String s, int start) {
        for(int i = start + first; i <= last; i++) {
            if(tokens.get(i).equals(s)) return i - first;
        }
        return -1;
    }

    public boolean contains(String s) {
        return tokens.contains(s);
    }

    public boolean contains(int type) {
        return types.contains(type);
    }

    public int getType(int i) {
        if(i < 0 || first + i > last) return -1;
        return types.get(first + i);
    }

    public String getToken(int i) {
        if(i < 0 || first + i > last) return "";
        return tokens.get(first + i);
    }

    public String getTokenUpper(int i) {
        if(i < 0 || i + first > last) return "";
        return tokens.get(first + i).toUpperCase();
    }

    public void setToken(int index, String token, int type) {
        tokens.set(first + index, token);
        types.set(first + index, type);
    }

    public int countTokens() {
        return (last == -1) ? 0 : last - first + 1;
    }

    public SQLTokenizer split(int start) {
        return new SQLTokenizer(this, first + start, last);
    }

    public SQLTokenizer split(int start, int end) {
        return new SQLTokenizer(this, first + start, first + end);
    }

    public SQLTokenizer append(String tokn, int type) {
        if(last != tokens.size() - 1) {
            synchronized(this) {
                tokens = new StringList(tokens, first, last + 1);
                types = new IntList(types, first, last + 1);
                first = 0;
                last = tokens.size() - 1;
            }
        }
        tokens.add(tokn);
        types.add(type);
        last++;
        return this;
    }

    public SQLTokenizer append(SQLTokenizer t) {
        if(last != tokens.size() - 1) {
            synchronized(this) {
                tokens = new StringList(tokens, first, last + 1);
                types = new IntList(types, first, last + 1);
                first = 0;
                last = tokens.size() - 1;
            }
        }
        synchronized(t) {
            tokens.add(t.tokens, t.first, t.last + 1);
            types.add(t.types, t.first, t.last + 1);
        }
        last = tokens.size() - 1;
        return this;
    }

    public String merge(String delim) {
        return merge(0, last - first, delim);
    }

    public String merge(int start, int end, String delim) {
        StringBuffer buf = new StringBuffer();
        for(int i = first + start; i <= first + end; i++) {
            if(i > first + start) buf.append(delim);
            buf.append(tokens.get(i));
        }
        return buf.toString();
    }

    /*public static SQLTokenizer merge(SQLTokenizer[] tzs) {
      // make me more efficient
      SQLTokenizer t = new SQLTokenizer();
      for(int i=0;tzs!=null && i<tzs.length;i++) {
        for(int n=tzs[i].first;n<=tzs[i].last;n++) {
          t.append(tzs[i].tokens.get(n),tzs[i].types.get(n));
        }
      }
      return t;
    }*/
    public SQLTokenizer[] parseOutsideBrackets(int delimiter) {
        ArrayList<SQLTokenizer> al = new ArrayList<SQLTokenizer>();
        SQLTokenizer tz = new SQLTokenizer();
        int depth = 0;
        for(int i = first; i <= last; i++) {
            if(types.get(i) == LEFTBRACKET) {
                depth++;
            } else if(types.get(i) == RIGHTBRACKET) {
                depth--;
            }
            if(depth == 0 && types.get(i) == delimiter) {
                al.add(tz);
                tz = new SQLTokenizer();
            } else {
                tz.append(tokens.get(i), types.get(i));
            }
        }
        al.add(tz);
        return al.toArray(new SQLTokenizer[0]);
    }

    public SQLTokenizer[] parse(int delimiter) {
        ArrayList<SQLTokenizer> al = new ArrayList<SQLTokenizer>();
        SQLTokenizer tz = new SQLTokenizer();
        for(int i = first; i <= last; i++) {
            if(types.get(i) == delimiter) {
                al.add(tz);
                tz = new SQLTokenizer();
            } else {
                tz.append(tokens.get(i), types.get(i));
            }
        }
        al.add(tz);
        return al.toArray(new SQLTokenizer[0]);
    }

    public SQLTokenizer removeRedundantBrackets() {
        if(types.get(first) != LEFTBRACKET || types.get(last) != RIGHTBRACKET) return this;
        int depth = 1;
        for(int i = first + 1; i <= last; i++) {
            switch(types.get(i)) {
                case LEFTBRACKET:
                    depth++;
                    break;
                case RIGHTBRACKET:
                    depth--;
                    break;
                default:
                    if(depth == 0) return this; /* there are no redundant brackets */
            }
        }
        first++;
        last--;
        return this.removeRedundantBrackets();
    }

    //
    public static final int LEFTBRACKET = 0;
    public static final int RIGHTBRACKET = 1;
    public static final int STRINGLITERAL = 2;
    public static final int COMMA = 3;
    public static final int SEMICOLON = 4;
    public static final int TEXT = 5;
    public static final int KEYWORD = 6;
    public static final int IDENTIFIER = 7;
    public static final int NOTEQ = 8;  // <> !=
    public static final int LTEQ = 9;  // <=
    public static final int GTEQ = 10; // >=
    public static final int LT = 11; // <
    public static final int GT = 12; // >
    public static final int EQ = 13; // =
    public static final int CONCAT = 14; // ||
    public static final int ADD = 15; // +
    public static final int SUB = 16; // -
    public static final int MUL = 17; // *
    public static final int DIV = 18; // /
    public static final int MOD = 19; // %
    public static final int COLON = 20;
}