package tempest.functions;

import tempest.data._DOUBLE;
import tempest.data._ROW;
import tempest.interfaces.Function;
import tempest.interfaces.TypedObject;
import tempest.logic.Group;

import java.sql.SQLException;
import java.util.ArrayList;

public class RANDOM implements Function {

    public RANDOM() {}

    public TypedObject performFunction(Group grp, ArrayList args) throws SQLException {
        throw new SQLException("Cant perform RANDOM(Group)");
    }

    public TypedObject performFunction(_ROW row, ArrayList args) throws SQLException {
        if(args == null || args.size() != 0) throw new SQLException("RANDOM : Wrong number of arguments");
        //
        return new _DOUBLE(Math.random());
    }

    public int numArgs() { return 0; }

    public String getName() { return "RANDOM"; }

    public int getDefaultReturnType() { return TypedObject.DOUBLE; }

    public boolean isDeterministic() { return false; }

    public boolean isAggregate() { return false; }
}