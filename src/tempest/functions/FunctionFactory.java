package tempest.functions;

import tempest.functions.aggregates.COUNT;
import tempest.interfaces.Function;

public class FunctionFactory {

    public FunctionFactory() {}

    public static Function createFunction(String name) {
        if(name == null) return null;
        name = name.toUpperCase();
        //try{
        if("ABS".equals(name)) {
            return new ABS();
        } else if("UPPER".equals(name)) {
            return new UPPER();
        } else if("LOWER".equals(name)) {
            return new LOWER();
        } else if("RANDOM".equals(name)) {
            return new RANDOM();
        } else if("MOD".equals(name)) {
            return new MOD();
        } else if("COUNT".equals(name)) {
            return new COUNT();
        }
        //}catch(Exception e) {
        //  Engine.getLogger().log("FunctionFactory("+name+").e="+e);
        //  throw new SQLException("'"+name+"' is not a recognized function");
        //}
        return null;
    }
}