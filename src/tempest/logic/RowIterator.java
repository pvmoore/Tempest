package tempest.logic;

import tempest.data._BOOLEAN;
import tempest.data._ROW;
import tempest.interfaces.TypedObject;
import tempest.interfaces.ValueExpression;
import tempest.sql.SelectList;
import tempest.sql.TableList;
import tempest.types.Table;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class RowIterator {
    private TableList tableList;
    private BooleanExpression where;
    private BooleanExpression having;
    private ValueExpression[] groupbyExpr;
    private ValueExpression[] orderbyExpr;
    private boolean[] orderbyDirs;
    private Table[] tables;
    private int tIndex;
    private int rIndex;
    private int gIndex;
    private ArrayList calculatedRows;
    private ArrayList<Group> groups;
    private boolean isGrouped;
    private int counter;

    public RowIterator(TableList tableList,
                       SelectList selectList,
                       ValueExpression[] groupbyExpr,
                       BooleanExpression where,
                       ValueExpression[] orderbyExpr,
                       boolean[] orderbyDirs,
                       BooleanExpression having)
    throws SQLException {
        this.tableList = tableList;
        this.where = where;
        this.groupbyExpr = groupbyExpr;
        this.having = having;
        this.orderbyExpr = orderbyExpr;
        this.orderbyDirs = orderbyDirs;
        tables = tableList.getTables();
        //
        if(groupbyExpr != null) {
            // there is grouping so need to calculate groups first
            // take account of 'havingExpr' here also
            isGrouped = true;
            groupRows();
            // result is a single table which can then be ordered
        } else if(selectList.containsAggregates()) {
            System.out.println("no GROUPBY but aggregates are being used");
            System.out.println("so create one huge group and evaluate group");
            isGrouped = true;
            _ROW row = null;
            Group grp = new Group();
            while((row = internalGetNextRow()) != null) {
                grp.addRow(row);
            }
            groups = new ArrayList<Group>();
            groups.add(grp);
            return;
        }
        //
        if(orderbyExpr != null && orderbyExpr.length > 0) {
            // if ordering results then we need to calculate all before returning any
            // otherwise we can go through sequentially
            orderRows();
        }
    }

    public boolean isGrouped() { return isGrouped; }

    /**
     * gets the next group in the result set
     * this is always pre-calculated into 'groups'
     *
     * @return next group
     * @throws SQLException
     */
    public Group getNextGroup() throws SQLException {
        //System.out.println("groups = "+groups+" counter="+counter);
        if(groups.size() <= counter) return null;
        return groups.get(counter++);
    }

    public _ROW getNextRow() throws SQLException {
        if(tables == null) return null;
        if(calculatedRows != null) {
            if(calculatedRows.size() > counter) {
                return (_ROW)calculatedRows.get(counter++);
            }
            return null;
        }
        //
        return internalGetNextRow();
    }

    //////////////////////////////////////////////////////////////////////////////
    private Group internalGetNextGroup() throws SQLException {
        if(groups.size() <= gIndex) return null;
        return groups.get(gIndex++);
    }

    private _ROW internalGetNextRow() throws SQLException {
        if(tables == null) return null;
        if(rIndex >= tables[tIndex].getCardinality()) {
            rIndex = 0;
            tIndex++;
        }
        if(tIndex >= tables.length) return null;
        //
        _ROW row = tables[tIndex].getRow(rIndex++);
        if(where != null) {
            _BOOLEAN valid = null;
            try {
                valid = (_BOOLEAN)where.evaluate(row);
            } catch(ClassCastException cce) {
                throw new SQLException("BOOLEAN value expected in WHERE");
            }
            if(valid.get() != _BOOLEAN.TRUE) {
                row = getNextRow();
            }
        }
        return row;
    }

    private void orderRows() throws SQLException {
        System.out.println("orderRows");
        SortTree tree = new SortTree(orderbyExpr, orderbyDirs);
        _ROW row = null;
        Group grp = null;
        if(calculatedRows == null) {
            // no preprocessing has been done
            if(isGrouped) {
                while((grp = internalGetNextGroup()) != null) {
                    // if we are ordering groups
                    // then use the top row in each group
                    tree.add(grp.getRow(0));
                }
            } else {
                while((row = internalGetNextRow()) != null) {
                    tree.add(row);
                }
            }
        } else {
            throw new SQLException("Unexpected assertion");
            // preprocessing has already been done
            //for(int i=0;i<calculatedRows.size();i++) {
            //  tree.add((_ROW)calculatedRows.get(i));
            //}
        }
        // not grouped any more
        isGrouped = false;
        calculatedRows = tree.getRowsInOrder();
    }

    private void groupRows() throws SQLException {
        _ROW row = null;
        Hashtable<String, Group> hash = new Hashtable<String, Group>(); // (groupby row values,Group)
        // group rows here
        while((row = internalGetNextRow()) != null) {
            String s = "";
            for(int i = 0; i < groupbyExpr.length; i++) {
                TypedObject o = groupbyExpr[i].evaluate(row);
                s += "," + o;
            }
            Group grp = hash.remove(s);
            if(grp == null) {
                grp = new Group();
            }
            grp.addRow(row);
            hash.put(s, grp);
        }
        // ok. we now have a Hashtable of (groupby row values,Group)
        // now apply 'having' and write results to 'calculatedRows'
        // so they can be ordered
        //calculatedRows = new ArrayList();
        groups = new ArrayList<Group>();
        Enumeration elements = hash.elements();
        while(elements.hasMoreElements()) {
            Group grp = (Group)elements.nextElement();
            // apply having to groups here
            //if(having.evaluateGroup(grp).equals(DataFactory.TRUE)) {
            groups.add(grp);
            //calculatedRows.add(grp.getRow(0));
            //}
        }
    }
}