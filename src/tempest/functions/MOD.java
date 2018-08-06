package tempest.functions;

import tempest.data.*;
import tempest.interfaces.Function;
import tempest.interfaces.TypedObject;
import tempest.interfaces.ValueExpression;
import tempest.logic.Group;

import java.sql.SQLException;
import java.util.ArrayList;

public class MOD implements Function {

    public MOD() {}

    public TypedObject performFunction(Group grp, ArrayList args) throws SQLException {
        throw new SQLException("Cant perform MOD(Group)");
    }

    public TypedObject performFunction(_ROW row, ArrayList args) throws SQLException {
        if(args == null || args.size() != 2) throw new SQLException("MOD : Wrong number of arguments");
        //
        ValueExpression ve1 = (ValueExpression)args.get(0);
        ValueExpression ve2 = (ValueExpression)args.get(1);
        TypedObject t1 = ve1.evaluate(row);
        TypedObject t2 = ve2.evaluate(row);
        if(t1 == null || t2 == null) return null;
        switch(t1.getType()) {
            case TypedObject.ARRAY:
                throw new SQLException("Can't MOD(ARRAY)");
            case TypedObject.BOOLEAN:
                throw new SQLException("Can't MOD(BOOLEAN)");
            case TypedObject.YMINTERVAL:
                throw new SQLException("Can't MOD(YMINTERVAL)");
            case TypedObject.DTINTERVAL:
                throw new SQLException("Can't MOD(DTINTERVAL)");
            case TypedObject.DOUBLE: {
                double d = ((_DOUBLE)t1).get();
                return new _DOUBLE(d % t2.getDouble());
            }
            case TypedObject.FLOAT: {
                float f = ((_FLOAT)t1).get();
                return new _FLOAT(f % t2.getFloat());
            }
            case TypedObject.INT: {
                int i = ((_INT)t1).get();
                return new _INT(i % t2.getInt());
            }
            case TypedObject.LONG: {
                long l = ((_LONG)t1).get();
                return new _LONG(l % t2.getLong());
            }
            case TypedObject.STRING:
                throw new SQLException("Can't MOD(STRING)");
            case TypedObject.TIMESTAMP:
                throw new SQLException("Can't MOD(TIMESTAMP)");
            case TypedObject.TIME:
                throw new SQLException("Can't MOD(TIME)");
            case TypedObject.DATE:
                throw new SQLException("Can't MOD(DATE)");
        }
        throw new SQLException("MOD : An error occurred");
    }

    public int numArgs() { return 2; }

    public String getName() { return "MOD"; }

    public int getDefaultReturnType() { return TypedObject.DOUBLE; }

    public boolean isDeterministic() { return true; }

    public boolean isAggregate() { return false; }
}
