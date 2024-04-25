package truffle_scheme6;

import truffle_scheme6.nodes.atoms.Identifier;

import java.util.HashMap;
import java.util.Map;

// todo
//  variables to add
//  nan.0
//  inf.0
public final class GlobalScopeObject {
    private final Map<Identifier, SchemeObject> vars = new HashMap<>();

    /**
     *
     * @param name name of the variable
     * @param val value of the variable
     * @return true if the variable is new. false if it is changing the value of an existing variable
     */
    public boolean newVar(Identifier name, SchemeObject val) {
        return vars.put(name, val) == null;
    }

    public SchemeObject getVar(Identifier name) {
        return vars.get(name);
    }
}
