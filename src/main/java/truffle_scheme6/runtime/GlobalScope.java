package truffle_scheme6.runtime;

import truffle_scheme6.SchemeNode;
import truffle_scheme6.nodes.atoms.SIdentifierLiteralNode;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

// todo
//  variables to add
//  nan.0
//  inf.0
public final class GlobalScope {
    private final Map<SIdentifierLiteralNode, SchemeNode> vars = new IdentityHashMap<>();

    /**
     *
     * @param name name of the variable
     * @param val value of the variable
     * @return true if the variable is new. false if it is changing the value of an existing variable
     */
    public boolean newVar(SIdentifierLiteralNode name, SchemeNode val) {
        return vars.put(name, val) == null;
    }

    public SchemeNode getVar(SIdentifierLiteralNode name) {
        return vars.get(name);
    }
}
