package tempest.functions.aggregates;

import tempest.data._ROW;
import tempest.interfaces.Function;
import tempest.interfaces.TypedObject;
import tempest.interfaces.ValueExpression;
import tempest.logic.Group;

import java.sql.SQLException;
import java.util.ArrayList;

public class COUNT implements Function {

    // possible args are:
    // count(*)
    // count([DISTINCT] col)
    // count([ALL] col)

    public COUNT() {}

    public TypedObject performFunction(Group grp, ArrayList args) throws SQLException {
        if(args == null || args.size() > 2) throw new SQLException("COUNT : Wrong number of arguments");
        //
        ValueExpression ve = (ValueExpression)args.get(0);
        return ve.evaluate(grp);
    }

    public TypedObject performFunction(_ROW row, ArrayList args) throws SQLException {
        throw new SQLException("Can't perform aggregate function on row");
        //TypedObject t = ve.evaluate(row);
        //if(t==null) return null;
        //if(t.getType()!=TypedObject.STRING) {
        // throw new SQLException("Can't convert "+t.getName()+" to LOWER case");
        //}
        //
        //return new _STRING(t.toString().toLowerCase());
        //return null;
    }

    public int numArgs() { return 1; } // !!!!

    public String getName() { return "COUNT"; }

    public int getDefaultReturnType() { return TypedObject.INT; }

    public boolean isDeterministic() { return false; }

    public boolean isAggregate() { return true; }

}