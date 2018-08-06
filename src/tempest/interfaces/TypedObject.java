package tempest.interfaces;

import tempest.data._BOOLEAN;

import java.sql.SQLException;

public interface TypedObject {
    public static enum Type {
        UNKNOWN,
        BOOLEAN,
        INT,
        FLOAT,
        LONG,
        DOUBLE,
        YMINTERVAL,
        DTINTERVAL,
        DATE,
        TIME,
        TIMESTAMP,
        STRING,
        ARRAY,
        ROW
    }

    public static final int UNKNOWN = -1;
    public static final int BOOLEAN = 0;
    public static final int INT = 1;
    public static final int FLOAT = 2;
    public static final int LONG = 3;
    public static final int DOUBLE = 4;
    public static final int YMINTERVAL = 5;
    public static final int DTINTERVAL = 6;
    public static final int DATE = 7;
    public static final int TIME = 8;
    public static final int TIMESTAMP = 9;
    public static final int STRING = 10;
    public static final int ARRAY = 11;
    public static final int ROW = 12;

    public int getType();

    //public boolean isNull();
    public String getName();

    public int getInt();

    public float getFloat();

    public long getLong();

    public double getDouble();

    //
    public TypedObject add(TypedObject t) throws SQLException;

    public TypedObject subtract(TypedObject t) throws SQLException;

    public TypedObject multiply(TypedObject t) throws SQLException;

    public TypedObject divide(TypedObject t) throws SQLException;

    public TypedObject concat(TypedObject t) throws SQLException;

    //
    public _BOOLEAN and(TypedObject o) throws SQLException;

    public _BOOLEAN or(TypedObject o) throws SQLException;

    public _BOOLEAN isLessThan(TypedObject o) throws SQLException;

    public _BOOLEAN isGreaterThan(TypedObject o) throws SQLException;

    public _BOOLEAN isLessThanOrEqual(TypedObject o) throws SQLException;

    public _BOOLEAN isGreaterThanOrEqual(TypedObject o) throws SQLException;

    public _BOOLEAN isEqual(TypedObject o) throws SQLException;
}