package tempest.interfaces;

import tempest.data._ROW;
import tempest.logic.Group;

import java.sql.SQLException;
import java.util.ArrayList;

public interface Function {
    public TypedObject performFunction(_ROW row, ArrayList args) throws SQLException; // ArrayList of ValueExpression

    public TypedObject performFunction(Group grp, ArrayList args) throws SQLException;

    public int numArgs();

    //public int maxArgs(); // may be necessary
    //public int minArgs();
    public int getDefaultReturnType();

    public String getName();

    public boolean isDeterministic();

    public boolean isAggregate();
}