package truffle_scheme6.runtime;

import java.util.HashMap;
import java.util.Map;

// todo
//  variables to add
//  nan.0
//  inf.0
public final class GlobalScope {
    private final Map<SSymbol, Object> vars = new HashMap<>();

    /**
     * @param name name of the variable
     * @param val  value of the variable
     * @return true if the variable is new. false if it is changing the value of an existing variable
     */
    public boolean setVar(SSymbol name, Object val) {
        return vars.put(name, val) == null;
    }

    public Object getVar(SSymbol name) {
        return vars.get(name);
    }
}
