package tempest.interfaces;

import tempest.data._ROW;
import tempest.logic.Group;
import tempest.sql.SQLTokenizer;
import tempest.sql.TableList;
import tempest.types.Column;

import java.sql.SQLException;

public interface ValueExpression {
    public TypedObject evaluate(_ROW row) throws SQLException;

    public TypedObject evaluate(Group grp) throws SQLException;

    //public void parseExpression(String s,TableList tableList) throws SQLException;
    public void parseExpression(SQLTokenizer tz, TableList tableList) throws SQLException;

    public TypedObject simplifyExpression();

    public int getReturnType();

    public Column getColumn();

    public String displayExpression(String pad);

    public boolean isAggregate();
}