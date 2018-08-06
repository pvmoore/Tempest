package tempest.sql;

import tempest.Engine;
import tempest.interfaces.ValueExpression;
import tempest.logic.NumericValueExpression;
import tempest.types.Column;

import java.sql.SQLException;
import java.util.ArrayList;

public class SelectList {
    private ArrayList<ValueExpression> valueExpressions = new ArrayList<ValueExpression>();
    private ArrayList<String> labels = new ArrayList<String>();
    private ArrayList<Integer> types = new ArrayList<Integer>();
    private boolean containsAggregates = false;

    public SelectList(TableList tableList, SQLTokenizer tz) throws SQLException {
        tz = expand(tableList, tz);
        SQLTokenizer[] tzs = tz.parseOutsideBrackets(SQLTokenizer.COMMA);
        //System.out.println("tzs.length="+tzs.length);
        if(tzs.length == 0) throw new SQLException("Syntax error");
        for(int i = 0; i < tzs.length; i++) {
            if(tzs[i].countTokens() == 0) throw new SQLException("Syntax error");
            int ASpos = tzs[i].indexOf(Keyword.AS);
            String label = null;
            if(ASpos != -1) {
                label = tzs[i].getToken(ASpos + 1);
                tzs[i] = tzs[i].split(0, ASpos - 1);
                //System.out.println("label="+label);
            }
            //System.out.println("tzs[i]="+tzs[i]);
            ValueExpression ve = new NumericValueExpression();
            ve.parseExpression(tzs[i], tableList);
            ve.simplifyExpression();
            Engine.getLogger().log("Expression = \n" + ve);
            //System.out.println("ve="+ve);
            if(label == null) {
                label = calcLabel(ve, i + 1);
            }
            labels.add(label);
            types.add(ve.getReturnType());
            valueExpressions.add(ve);
            if(ve.isAggregate()) containsAggregates = true;
        }
    }

    private SQLTokenizer expand(TableList tableList, SQLTokenizer tz) throws SQLException {
        //System.out.println("expand "+tz);
        if(tz.getToken(0).equals("*")) {
            Column[] cols = tableList.getAllColumns();
            SQLTokenizer t = new SQLTokenizer();
            for(int i = 0; cols != null && i < cols.length; i++) {
                if(i > 0) t.append(",", SQLTokenizer.COMMA);
                t.append(cols[i].getName(), SQLTokenizer.IDENTIFIER);
            }
            tz = t.append(tz.split(1));
        }
        //System.out.println("expanded to "+tz);
        //System.out.println("tokens are now = "+tz);
        for(int i = 0; i < tz.countTokens() - 1; i++) {
            if(tz.getToken(i).endsWith(".") && "*".equals(tz.getToken(i + 1))) {
                //System.out.println("expanding "+tz);
                SQLTokenizer t = new SQLTokenizer();
                String name = tz.getToken(i);
                name = name.substring(0, name.length() - 1);
                Column[] cols = tableList.getColumns(name);
                for(int n = 0; n < cols.length; n++) {
                    if(n > 0) t.append(",", SQLTokenizer.COMMA);
                    t.append(cols[n].getName(), SQLTokenizer.IDENTIFIER);
                }
                SQLTokenizer temp = null;
                if(i > 0) {
                    temp = tz.split(0, i - 1);
                } else {
                    temp = new SQLTokenizer();
                }
                temp.append(t);
                temp.append(tz.split(i + 2));
                tz = temp;
            }
        }
        return tz;
    }

    private String calcLabel(ValueExpression ve, int index) {
        Column c = ve.getColumn();
        if(c != null) return c.getName();
        return "COL" + index;
    }

    public boolean containsAggregates() { return containsAggregates; }

    public String[] getLabels() {
        return labels.toArray(new String[0]);
    }

    public ValueExpression[] getValueExpressions() {
        return valueExpressions.toArray(new ValueExpression[0]);
    }

    public Integer[] getTypes() {
        return types.toArray(new Integer[0]);
    }
}