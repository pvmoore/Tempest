package tempest.logic;

import tempest.data.DataFactory;
import tempest.data._BOOLEAN;
import tempest.data._ROW;
import tempest.functions.FunctionFactory;
import tempest.interfaces.Function;
import tempest.interfaces.TypedObject;
import tempest.interfaces.ValueExpression;
import tempest.sql.Keyword;
import tempest.sql.SQLTokenizer;
import tempest.sql.TableList;
import tempest.types.Column;

import java.sql.SQLException;
import java.util.ArrayList;

public class BooleanExpression implements ValueExpression {
    public static final int NOP = 0;
    public static final int GT = 1; // >
    public static final int LT = 2; // <
    public static final int EQ = 3; // =
    public static final int LTEQ = 4; // <=
    public static final int GTEQ = 5; // >=
    public static final int NOTEQ = 6; // <> or !=
    public static final int AND = 7;
    public static final int OR = 8;
    public static final int NOT = 9; // todo
    //
    private Column column = null;
    private Function function = null;
    private TypedObject literal = null;
    private ArrayList<ValueExpression> functionColumns = null;
    private ValueExpression left = null;
    private ValueExpression right = null;
    private int operator = NOP;
    private int OPERATORpos = -1;
    private String operatorString = null;
    private boolean isAggregate = false;

    public BooleanExpression() {}

    public void parseExpression(SQLTokenizer tz, TableList tableList) throws SQLException {
        //System.out.println("parseExpression() tz="+tz);
        if(tz.countTokens() == 0) throw new SQLException("No columns specified in WHERE");
        tz.removeRedundantBrackets();
        getOperator(tz);
        //System.out.println("operator="+operator+" "+operatorString+" "+OPERATORpos);
        if(operator == NOP) {
            int lb = -1;
            if((lb = tz.indexOf(SQLTokenizer.LEFTBRACKET)) != -1 &&
                (function = FunctionFactory.createFunction(tz.getToken(0))) != null) {
                int rb = tz.lastIndexOf(SQLTokenizer.RIGHTBRACKET);
                if(rb == -1) throw new SQLException("Missing ')'");
                if(function.isAggregate()) isAggregate = true;
                SQLTokenizer tz2 = tz.split(lb + 1, rb - 1);
                functionColumns = new ArrayList<ValueExpression>();
                splitColumns(tableList, tz2);
            } else {
                int type = DataFactory.parseTypeOfValue(tz);
                if(type == -1) {
                    if(tableList == null) throw new SQLException("No tables specified in query");
                    column = tableList.getColumn(tz.getToken(0));
                } else {
                    literal = DataFactory.createTypedObject(type, tz);
                }
            }
        } else {
            //operatorString = tz.getToken(OPERATORpos);
            System.out.println("operatorString=" + operatorString);
            if(operator == OR || operator == AND) { // boolean
                left = new BooleanExpression();
                right = new BooleanExpression();
            } else { // value
                left = new NumericValueExpression();
                right = new NumericValueExpression();
            }
            left.parseExpression(tz.split(0, OPERATORpos - 1), tableList);
            right.parseExpression(tz.split(OPERATORpos + 1), tableList);
        }
    }

    public String toString() {
        return "BOOLEANEXPRESSION\n" + this.displayExpression("  ") + "END";
    }

    public boolean isAggregate() { return isAggregate; }

    public int getReturnType() { return TypedObject.BOOLEAN; }

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
        return null;
    }

    //public TypedObject evaluate(Group group) throws SQLException {
    //  return DataFactory.FALSE;
    //}
    public TypedObject evaluate(Group grp) throws SQLException {
        throw new SQLException("Can't evaluate group yet");
    }

    public TypedObject evaluate(_ROW row) throws SQLException {
        if(literal != null) return literal;
        if(column != null) {
            if(row == null) return null;
            return row.get(column.getName());
        }
        if(function != null) {
            return function.performFunction(row, functionColumns);
        }
        TypedObject lha = left.evaluate(row);
        TypedObject rha = right.evaluate(row);
        if(lha == null || rha == null) return new _BOOLEAN(_BOOLEAN.UNKNOWN);
        _BOOLEAN result = null;
        switch(operator) {
            case AND:
                result = lha.and(rha);
                break;
            case OR:
                result = lha.or(rha);
                break;
            case LT:
                result = lha.isLessThan(rha);
                break;
            case GT:
                result = lha.isGreaterThan(rha);
                break;
            case LTEQ:
                result = lha.isLessThanOrEqual(rha);
                break;
            case GTEQ:
                result = lha.isGreaterThanOrEqual(rha);
                break;
            case EQ:
                result = lha.isEqual(rha);
                break;
        }
        // if NOT then result.negate();
        return result;
    }

    /**
     * This ValueExpression is a function.
     * This method splits function arguments into 'functionColumns'
     */
    private void splitColumns(TableList tableList, SQLTokenizer t)
    throws SQLException {
        functionColumns = new ArrayList<ValueExpression>();
        if(t.countTokens() == 0) return; // no args
        SQLTokenizer[] tkz = t.parseOutsideBrackets(SQLTokenizer.COMMA);
        for(int i = 0; i < tkz.length; i++) {
            ValueExpression ve = new NumericValueExpression();
            ve.parseExpression(tkz[i], tableList);
            functionColumns.add(ve);
        }
    }

    private void getOperator(SQLTokenizer tz) {
        if((OPERATORpos = tz.indexOfOB(Keyword.OR)) != -1) {
            operator = OR;
            operatorString = "OR";
            return;
        }
        if((OPERATORpos = tz.indexOfOB(Keyword.AND)) != -1) {
            operator = AND;
            operatorString = "AND";
            return;
        }
        if((OPERATORpos = tz.indexOfOB(SQLTokenizer.NOTEQ)) != -1) {
            operator = NOTEQ;
            operatorString = "<>";
            return;
        }
        if((OPERATORpos = tz.indexOfOB(SQLTokenizer.LTEQ)) != -1) {
            operator = LTEQ;
            operatorString = "<=";
            return;
        }
        if((OPERATORpos = tz.indexOfOB(SQLTokenizer.GTEQ)) != -1) {
            operator = GTEQ;
            operatorString = ">=";
            return;
        }
        if((OPERATORpos = tz.indexOfOB(SQLTokenizer.LT)) != -1) {
            operator = LT;
            operatorString = "<";
            return;
        }
        if((OPERATORpos = tz.indexOfOB(SQLTokenizer.GT)) != -1) {
            operator = GT;
            operatorString = ">";
            return;
        }
        if((OPERATORpos = tz.indexOfOB(SQLTokenizer.EQ)) != -1) {
            operator = EQ;
            operatorString = "=";
            return;
        }
    }
}