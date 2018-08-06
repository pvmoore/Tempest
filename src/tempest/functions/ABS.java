package tempest.functions;

import tempest.data.*;
import tempest.interfaces.Function;
import tempest.interfaces.TypedObject;
import tempest.interfaces.ValueExpression;
import tempest.logic.Group;

import java.sql.SQLException;
import java.util.ArrayList;

public class ABS implements Function {

    public ABS() {}

    public TypedObject performFunction(Group grp, ArrayList args) throws SQLException {
        throw new SQLException("Cant perform ABS(Group)");
    }

    public TypedObject performFunction(_ROW row, ArrayList args) throws SQLException {
        if(args == null || args.size() != 1) throw new SQLException("ABS : Wrong number of arguments");
        //
        ValueExpression ve = (ValueExpression)args.get(0);
        TypedObject t = ve.evaluate(row);
        if(t == null) return null;
        switch(t.getType()) {
            case TypedObject.ARRAY:
                throw new SQLException("Can't ABS(ARRAY)");
            case TypedObject.BOOLEAN:
                throw new SQLException("Can't ABS(BOOLEAN)");
            case TypedObject.DOUBLE: {
                double d = ((_DOUBLE)t).get();
                if(d > 0) return new _DOUBLE(d);
                else return new _DOUBLE(-d);
            }
            case TypedObject.FLOAT: {
                float f = ((_FLOAT)t).get();
                if(f > 0) return new _FLOAT(f);
                else return new _FLOAT(-f);
            }
            case TypedObject.INT: {
                int i = ((_INT)t).get();
                if(i > 0) return new _INT(i);
                else return new _INT(-i);
            }
            case TypedObject.YMINTERVAL: {
                long l = ((_YMINTERVAL)t).get();
                if(l > 0) return new _YMINTERVAL(l);
                return new _YMINTERVAL(-l);
            }
            case TypedObject.LONG: {
                long l = ((_LONG)t).get();
                if(l > 0) return new _LONG(l);
                else return new _LONG(-l);
            }
            case TypedObject.TIME:
                throw new SQLException("Can't ABS(TIME)");
            case TypedObject.DATE:
                throw new SQLException("Can't ABS(DATE)");
            case TypedObject.STRING:
                throw new SQLException("Can't ABS(STRING)");
            case TypedObject.TIMESTAMP:
                throw new SQLException("Can't ABS(TIMESTAMP)");
        }
        throw new SQLException("ABS : An error occurred");
    }

    public int numArgs() { return 1; }

    public String getName() { return "ABS"; }

    public int getDefaultReturnType() { return TypedObject.INT; }

    public boolean isDeterministic() { return true; }

    public boolean isAggregate() { return false; }
}