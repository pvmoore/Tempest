package tempest.interfaces;

import tempest.data._BOOLEAN;

import java.sql.SQLException;

public interface TypedObject {
    enum Type {
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

    int UNKNOWN = -1;
    int BOOLEAN = 0;
    int INT = 1;
    int FLOAT = 2;
    int LONG = 3;
    int DOUBLE = 4;
    int YMINTERVAL = 5;
    int DTINTERVAL = 6;
    int DATE = 7;
    int TIME = 8;
    int TIMESTAMP = 9;
    int STRING = 10;
    int ARRAY = 11;
    int ROW = 12;

    int getType();

    //boolean isNull();
    String getName();

    int getInt();

    float getFloat();

    long getLong();

    double getDouble();

    //
    TypedObject add(TypedObject t) throws SQLException;

    TypedObject subtract(TypedObject t) throws SQLException;

    TypedObject multiply(TypedObject t) throws SQLException;

    TypedObject divide(TypedObject t) throws SQLException;

    TypedObject concat(TypedObject t) throws SQLException;

    //
    _BOOLEAN and(TypedObject o) throws SQLException;

    _BOOLEAN or(TypedObject o) throws SQLException;

    _BOOLEAN isLessThan(TypedObject o) throws SQLException;

    _BOOLEAN isGreaterThan(TypedObject o) throws SQLException;

    _BOOLEAN isLessThanOrEqual(TypedObject o) throws SQLException;

    _BOOLEAN isGreaterThanOrEqual(TypedObject o) throws SQLException;

    _BOOLEAN isEqual(TypedObject o) throws SQLException;
}