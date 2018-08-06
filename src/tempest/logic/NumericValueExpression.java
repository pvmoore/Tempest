package tempest.logic;

import tempest.data.DataFactory;
import tempest.data._ROW;
import tempest.functions.FunctionFactory;
import tempest.interfaces.Function;
import tempest.interfaces.TypedObject;
import tempest.interfaces.ValueExpression;
import tempest.sql.SQLTokenizer;
import tempest.sql.TableList;
import tempest.types.Column;

import java.sql.SQLException;
import java.util.ArrayList;

public class NumericValueExpression implements ValueExpression {
    public static final int NOP = 0;
    public static final int ADD = 1;
    public static final int SUB = 2;
    public static final int DIV = 3;
    public static final int MUL = 4;
    public static final int CONCAT = 5; // ||

    private Function function = null;
    private TypedObject literal = null;
    private Column column = null;
    private NumericValueExpression left = null;
    private NumericValueExpression right = null;
    private int operator = NOP;
    private int OPERATORpos = -1;
    private String operatorString = "";
    private ArrayList<ValueExpression> functionColumns = null;
    private boolean isAggregate = false;

    public NumericValueExpression() {}

    public String toString() {
        return "EXPRESSION\n" + this.displayExpression("  ") + "END";
    }

    public void parseExpression(SQLTokenizer tz, TableList tableList) throws SQLException {
        if(tz.countTokens() == 0) throw new SQLException("No columns specified in SELECT");
        tz.removeRedundantBrackets();
        getOperator(tz);
        if(operator == NOP) {
            int lb = -1;
            if((lb = tz.indexOf(SQLTokenizer.LEFTBRACKET)) != -1 &&
                (function = FunctionFactory.createFunction(tz.getToken(0))) != null) {
                int rb = tz.lastIndexOf(SQLTokenizer.RIGHTBRACKET);
                if(rb == -1) throw new SQLException("Missing ')'");
                if(function.isAggregate()) {
                    isAggregate = true;
                }
                SQLTokenizer tz2 = tz.split(lb + 1, rb - 1);
                splitColumns(tableList, tz2);
            } else {
                //System.out.println("parsing type of ["+tz+"] tz="+tz);
                int type = DataFactory.parseTypeOfValue(tz);
                //System.out.println("type="+DataFactory.prettyTypeName(type));
                //
                if(type == -1) {
                    // this is a column reference
                    if(tableList == null) throw new SQLException("No tables specified in query");
                    column = tableList.getColumn(tz.getToken(0));
                } else {
                    literal = DataFactory.createTypedObject(type, tz);
                }
            }
        } else {
            //operatorString = tz.getToken(OPERATORpos);
            left = new NumericValueExpression();
            right = new NumericValueExpression();
            //
            left.parseExpression(tz.split(0, OPERATORpos - 1), tableList);
            right.parseExpression(tz.split(OPERATORpos + 1), tableList);
        }
    }

    public boolean isAggregate() { return isAggregate; }

    public String displayExpression(String pad) {
        StringBuffer buf = new StringBuffer();
        if(function != null) {
            buf.append(pad + function.getName() + "(" + function.numArgs() + " args)\n");
            for(int i = 0; i < functionColumns.size(); i++) {
                ValueExpression ve = functionColumns.get(i);
                buf.append(ve.displayExpression(pad + "  "));
            }
        } else if(literal != null) {
            buf.append(pad + "LITERAL = " + literal + " (" + literal.getName() + ")\n");
        } else if(column != null) {
            buf.append(pad + "COLUMNNAME OR ALIAS = " + column + "\n");
        } else {
            if(left != null) buf.append(left.displayExpression(pad + "  "));
            buf.append(pad + operatorString + "\n");
            if(right != null) buf.append(right.displayExpression(pad + "  "));
        }
        return buf.toString();
    }

    public Column getColumn() {
        return column;
    }

    public int getReturnType() {
        if(literal != null) {
            return literal.getType();
        }
        if(column != null) {
            return column.getType();
        }
        if(function != null) {
            int max = -1;
            for(int i = 0; i < functionColumns.size(); i++) {
                ValueExpression ve = functionColumns.get(i);
                int type = ve.getReturnType();
                if(type > max) max = type;
            }
            if(max == -1) return function.getDefaultReturnType();
            return max;
        }
        int lt = left.getReturnType();
        int rt = right.getReturnType();
        return Math.max(lt, rt);
    }

    public TypedObject simplifyExpression() {
        if(literal != null) {
            return literal;
        }
        if(column != null) {
            return null;
        }
        if(function != null) {
            try {
                for(int i = 0; i < functionColumns.size(); i++) {
                    ValueExpression ve = functionColumns.get(i);
                    ve.simplifyExpression();
                }
                if(!function.isDeterministic()) return null;
                TypedObject fResult = function.performFunction((_ROW)null, functionColumns);
                if(fResult != null) {
                    function = null;
                    functionColumns = null;
                    literal = fResult;
                }
                return fResult;
            } catch(Exception e) {

            }
            return null;
        }
        TypedObject lv = null;
        TypedObject rv = null;
        if(left != null) {
            lv = left.simplifyExpression();
        }
        if(right != null) {
            rv = right.simplifyExpression();
        }
        if(lv == null || rv == null) return null;
        //
        try {
            TypedObject o = evaluate((_ROW)null);
            if(o != null) {
                literal = o;
                left = null;
                right = null;
                return literal;
            }
        } catch(Exception e) {

        }
        return null;
    }

    public TypedObject evaluate(Group grp) throws SQLException {
        if(function != null) {
            return function.performFunction(grp, functionColumns);
        }
        return continueEvaluation(grp.getRow(0));
    }

    public TypedObject evaluate(_ROW row) throws SQLException {
        //System.out.println("evaluate function="+function+" literal="+literal+" column="+column);
        if(function != null) {
            return function.performFunction(row, functionColumns);
        }
        return continueEvaluation(row);
    }

    private TypedObject continueEvaluation(_ROW row) throws SQLException {
        if(literal != null) {
            return literal;
        }
        if(column != null) {
            if(row == null) return null;
            //System.out.println("row.get("+column.getName()+") = "+row.get(column.getName()));
            return row.get(column.getName());
        }
        TypedObject lha = left.evaluate(row);
        if(lha == null) return null;
        TypedObject rha = right.evaluate(row);
        if(rha == null) return null;
        TypedObject result = null;
        //
        switch(operator) {
            case MUL:
                result = lha.multiply(rha);
                break;
            case DIV:
                result = lha.divide(rha);
                break;
            case ADD:
                result = lha.add(rha);
                break;
            case SUB:
                result = lha.subtract(rha);
                break;
            case CONCAT:
                result = lha.concat(rha);
                break;
        }
        //System.out.println("evaluate "+lha+" "+operatorString+" "+rha+" = "+result);
        return result;
    }

    private void getOperator(SQLTokenizer tz) {
        if((OPERATORpos = tz.indexOfOB(SQLTokenizer.CONCAT)) != -1) {
            operator = CONCAT;
            operatorString = "||";
            return;
        }
        if((OPERATORpos = tz.indexOfOB(SQLTokenizer.ADD)) != -1) {
            operator = ADD;
            operatorString = "+";
            return;
        }
        if((OPERATORpos = tz.indexOfOB(SQLTokenizer.SUB)) != -1) {
            operator = SUB;
            operatorString = "-";
            return;
        }
        if((OPERATORpos = tz.indexOfOB(SQLTokenizer.MUL)) != -1) {
            operator = MUL;
            operatorString = "*";
            return;
        }
        if((OPERATORpos = tz.indexOfOB(SQLTokenizer.DIV)) != -1) {
            operator = DIV;
            operatorString = "/";
            return;
        }
    }

    /**
     * This ValueExpression is a function.
     * This method splits function arguments into 'functionColumns'
     */
    private void splitColumns(TableList tableList, SQLTokenizer t)
    throws SQLException {
        functionColumns = new ArrayList<ValueExpression>();
        if(t.countTokens() == 0) {
            // no args
            return;
        }
        if("COUNT".equals(function.getName())) {
            // special case for count
            ValueExpression ve = new CountValueExpression();
            ve.parseExpression(t, tableList);
            functionColumns.add(ve);
            return;
        }
        SQLTokenizer[] tkz = t.parseOutsideBrackets(SQLTokenizer.COMMA);
        for(int i = 0; i < tkz.length; i++) {
            ValueExpression ve = new NumericValueExpression();
            ve.parseExpression(tkz[i], tableList);
            functionColumns.add(ve);
        }
    }
}