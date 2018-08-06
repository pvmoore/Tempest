package tempest.functions;

import tempest.data._ROW;
import tempest.data._STRING;
import tempest.interfaces.Function;
import tempest.interfaces.TypedObject;
import tempest.interfaces.ValueExpression;
import tempest.logic.Group;

import java.sql.SQLException;
import java.util.ArrayList;

public class UPPER implements Function {

    public UPPER() {}

    public TypedObject performFunction(Group grp, ArrayList args) throws SQLException {
        throw new SQLException("Cant perform UPPER(Group)");
    }

    public TypedObject performFunction(_ROW row, ArrayList args) throws SQLException {
        if(args == null || args.size() != 1) throw new SQLException("UPPER : Wrong number of arguments");
        //
        ValueExpression ve = (ValueExpression)args.get(0);
        TypedObject t = ve.evaluate(row);
        if(t == null) return null;
        if(t.getType() != TypedObject.STRING) {
            throw new SQLException("Can't convert " + t.getName() + " to UPPER case");
        }
        //
        return new _STRING(t.toString().toUpperCase());
    }

    public int numArgs() { return 1; }

    public String getName() { return "UPPER"; }

    public int getDefaultReturnType() { return TypedObject.STRING; }

    public boolean isDeterministic() { return true; }

    public boolean isAggregate() { return false; }
}