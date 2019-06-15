package tempest.sql;

import tempest.Session;
import tempest.Tools;
import tempest.data._ROW;
import tempest.interfaces.TypedObject;
import tempest.interfaces.ValueExpression;
import tempest.logic.BooleanExpression;
import tempest.logic.Group;
import tempest.logic.NumericValueExpression;
import tempest.logic.RowIterator;
import tempest.types.Column;
import tempest.types.Table;

import java.sql.SQLException;
import java.util.ArrayList;

public class SQLSelect extends SQL {
    private Session session = null;
    private Table virtualTable = null;

    private int WHEREpos = -1;
    private int FROMpos = -1;
    private int JOINpos = -1;
    private int ORDERBYpos = -1;
    private int ORDERBYendpos = -1;
    private int GROUPBYpos = -1;
    private int GROUPBYendpos = -1;
    private int HAVINGpos = -1;
    private SQLTokenizer ORDERBYexpression = null;
    private SQLTokenizer HAVINGcondition = null;
    private SQLTokenizer GROUPBYexpression = null;
    private SQLTokenizer WHEREcondition = null;
    private SQLTokenizer JOINcondition = null;
    private SQLTokenizer tableListTokens = null;
    private SelectList SelectList = null;
    //
    private String[] labels = null;
    private Integer[] types = null;
    private ValueExpression[] valueExpressions = null;
    //
    private TableList tableList = null;
    private BooleanExpression where = null;
    private BooleanExpression having = null;
    //
    private ValueExpression[] groupbyExpressions = null;
    //
    private boolean[] directions = null;
    private ValueExpression[] orderExpressions = null;
    //
    private boolean isAggregate = false;
    private SQLTokenizer tz = null;

    public SQLSelect(Session session, SQLTokenizer tz) throws SQLException {
        this.session = session;
        this.tz = tz;
        log("***********************************************************************");
        log("SQLSelect [SELECT " + tz + "]");
        log("***********************************************************************");
        FROMpos = tz.indexOf(Keyword.FROM);
        if(FROMpos != -1) {
            WHEREpos = tz.indexOf(Keyword.WHERE);
            JOINpos = tz.indexOf(Keyword.JOIN);
            ORDERBYpos = tz.indexOf(Keyword.ORDER);

            if(ORDERBYpos != -1) {
                if(tz.getType(ORDERBYpos + 1) != Keyword.BY) {
                    throw new SQLException("Syntax error at " + tz.getToken(ORDERBYpos));
                }
                ORDERBYendpos = ORDERBYpos + 1;
            }
            GROUPBYpos = tz.indexOf(Keyword.GROUP);
            if(GROUPBYpos != -1) {
                if(tz.getType(GROUPBYpos + 1) != Keyword.BY) {
                    throw new SQLException("Syntax error at " + tz.getToken(GROUPBYpos));
                }
                GROUPBYendpos = GROUPBYpos + 1;
                HAVINGpos = tz.indexOf(Keyword.HAVING, GROUPBYpos);
            }
        }
        if(FROMpos == -1 && (JOINpos != -1 || WHEREpos != -1 || GROUPBYpos != -1 || ORDERBYpos != -1 || HAVINGpos != -1)) {
            throw new SQLException("Missing 'FROM'");
        }
        if(ORDERBYpos != -1) {
            ORDERBYexpression = tz.split(ORDERBYpos + 2);
            tz = tz.split(0, ORDERBYpos - 1);
        }
        if(HAVINGpos != -1) {
            HAVINGcondition = tz.split(HAVINGpos + 1);
            tz = tz.split(0, HAVINGpos - 1);
        }
        if(GROUPBYpos != -1) {
            GROUPBYexpression = tz.split(GROUPBYpos + 2);
            tz = tz.split(0, GROUPBYpos - 1);
        }
        if(WHEREpos != -1) {
            WHEREcondition = tz.split(WHEREpos + 1);
            //System.out.println("WHEREcondition="+WHEREcondition);
            tz = tz.split(0, WHEREpos - 1);
        }
        if(JOINpos != -1) {
            JOINcondition = tz.split(JOINpos + 1);
            tz = tz.split(0, JOINpos - 1);
        }
        if(FROMpos != -1) {
            tableListTokens = tz.split(FROMpos + 1);
            tz = tz.split(0, FROMpos - 1);
            calcTableList();
        }
        //expandSelectList();
        //splitValueExpressions();
        SelectList = new SelectList(tableList, tz);
        valueExpressions = SelectList.getValueExpressions();
        labels = SelectList.getLabels();
        types = SelectList.getTypes();
        buildWhereExpression();
        buildGroupbyExpression();
        buildHavingExpression();
        buildOrderExpressions();
        //System.out.println("valueExpressions="+valueExpressions.length);
        //
        virtualTable = session.getSchema().createTable("VIRTUAL" + System.currentTimeMillis());
        for(int i = 0; i < labels.length; i++) {
            Column col = new Column(labels[i], types[i].intValue());
            virtualTable.addColumn(col);
        }
    }

    private void buildWhereExpression() throws SQLException {
        if(WHEREcondition == null) return;
        where = new BooleanExpression();
        where.parseExpression(WHEREcondition, tableList);
        where.simplifyExpression();
        System.out.println("where = \n" + where);
    }

    private void buildHavingExpression() throws SQLException {
        if(HAVINGcondition == null) return;
        having = new BooleanExpression();
        having.parseExpression(HAVINGcondition, tableList);
        having.simplifyExpression();
        System.out.println("having = \n" + having);
    }

    private void buildGroupbyExpression() throws SQLException {
        if(GROUPBYexpression == null) return;
        ArrayList<ValueExpression> al = new ArrayList<ValueExpression>();
        SQLTokenizer[] tks = GROUPBYexpression.parseOutsideBrackets(SQLTokenizer.COMMA);
        for(int i = 0; i < tks.length; i++) {
            NumericValueExpression nve = new NumericValueExpression();
            nve.parseExpression(tks[i], tableList);
            nve.simplifyExpression();
            System.out.println("groupby=" + nve);
            al.add(nve);
        }
        groupbyExpressions = al.toArray(new NumericValueExpression[0]);
    }

    private void buildOrderExpressions() throws SQLException {
        if(ORDERBYexpression == null) return;
        ArrayList<ValueExpression> al = new ArrayList<ValueExpression>();
        ArrayList<Boolean> dirs = new ArrayList<Boolean>();
        SQLTokenizer[] tks = ORDERBYexpression.parseOutsideBrackets(SQLTokenizer.COMMA);
        for(int i = 0; i < tks.length; i++) {
            boolean ascending = true;
            if(tks[i].getType(tks[i].countTokens() - 1) == Keyword.DESC) {
                tks[i] = tks[i].split(0, tks[i].countTokens() - 1);
                ascending = false;
            } else if(tks[i].getType(tks[i].countTokens() - 1) == Keyword.ASC) {
                tks[i] = tks[i].split(0, tks[i].countTokens() - 1);
            }
            //
            if(tks[i].countTokens() == 1) {
                int columnIndex = Tools.parseInt(tks[i].getToken(0), Integer.MAX_VALUE);
                if(columnIndex != Integer.MAX_VALUE) {
                    System.out.println("this is a column index");
                    if(columnIndex < 1 || columnIndex > labels.length) {
                        throw new SQLException("Can't order by non-existent column index " + columnIndex);
                    }
                    tks[i].setToken(0, labels[columnIndex - 1], SQLTokenizer.TEXT);
                }
            }
            //
            NumericValueExpression nve = new NumericValueExpression();
            nve.parseExpression(tks[i], tableList);
            nve.simplifyExpression();
            System.out.println("orderby=" + nve);
            System.out.println("direction=" + (ascending ? "ASC" : "DESC"));
            al.add(nve);
            dirs.add(ascending);
        }
        orderExpressions = al.toArray(new NumericValueExpression[0]);
        directions = new boolean[dirs.size()];
        for(int i = 0; i < directions.length; i++) {
            directions[i] = (dirs.get(i)).booleanValue();
        }
    }

    /**
     * build list of tables and correlations in the FROM clause
     *
     * @throws SQLException
     */
    private void calcTableList() throws SQLException {
        tableList = new TableList(session);
        SQLTokenizer[] tzs = tableListTokens.parse(SQLTokenizer.COMMA);
        for(int i = 0; tzs != null && i < tzs.length; i++) {
            if(tzs[i].countTokens() == 0) throw new SQLException("Syntax error");
            String correlation = null;
            String table = tzs[i].getToken(0);
            int n = 1;
            if(tzs[i].getType(1) == Keyword.AS) {
                n++;
            }
            correlation = tzs[i].getToken(n);
            tableList.addTable(table, correlation);
        }
    }

    public String toString() {
        return "SELECT[\n" +
            "  SELECT   = " + SelectList + "\n" +
            "  FROM     = " + tableList + "\n" +
            "  JOIN     = " + JOINcondition + "\n" +
            "  WHERE    = " + WHEREcondition + "\n" +
            "  GROUP BY = " + GROUPBYexpression + "\n" +
            "  HAVING   = " + HAVINGcondition + "\n" +
            "  ORDER BY = " + ORDERBYexpression + "\n" +
            "]";
    }

    //public boolean hasWhereClause()    { return wherePos!=-1; }
    //public boolean hasFromClause()     { return fromPos!=-1; }
    //public boolean hasAnsiJoinClause() { return joinPos!=-1; }
    //public boolean hasOrderbyClause()  { return orderbyPos!=-1; }
    //public boolean hasGroupbyClause()  { return groupbyPos!=-1; }
    //public boolean hasHavingClause()   { return havingPos!=-1; }
    public TableList getTableList() { return tableList; }

    public ValueExpression[] getValueExpressions() { return valueExpressions; }

    public Table execute() throws SQLException {
        virtualTable.truncate();
        //
        RowIterator rows = new RowIterator(tableList, SelectList, groupbyExpressions, where, orderExpressions, directions, having);
        _ROW src = null;
        Group grp = null;
        if(rows.isGrouped()) {
            //System.out.println("grouped");
            while((grp = rows.getNextGroup()) != null) {
                //System.out.println("grp="+grp);
                _ROW dest = virtualTable.createRow();
                for(int v = 0; v < valueExpressions.length; v++) {
                    TypedObject o = valueExpressions[v].evaluate(grp);
                    dest.set(v, o);
                }
                virtualTable.addRow(dest, session);
            }
        } else {
            while((src = rows.getNextRow()) != null) {
                _ROW dest = virtualTable.createRow();
                for(int v = 0; v < valueExpressions.length; v++) {
                    TypedObject o = valueExpressions[v].evaluate(src);
                    dest.set(v, o);
                }
                virtualTable.addRow(dest, session);
            }
        }
        return virtualTable;
    }
}