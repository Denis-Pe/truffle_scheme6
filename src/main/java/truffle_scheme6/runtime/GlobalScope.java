package truffle_scheme6.runtime;

import java.util.*;

public final class GlobalScope {
    private final Map<SSymbol, Object> vars = new IdentityHashMap<>();
    private SLibrary activeLibrary;
    public final List<SLibrary> availableLibraries = new ArrayList<>();

    public GlobalScope(SLibrary activeLibrary) {
        this.activeLibrary = activeLibrary;
        var base = new SLibrary(SPair.list(
                SSymbol.get("rnrs"),
                SSymbol.get("base"),
                SList.list(6)));
        availableLibraries.add(base);
    }

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

    public SLibrary getActiveLibrary() {
        return activeLibrary;
    }

    public void setActiveLibrary(SLibrary activeLibrary) {
        this.activeLibrary = activeLibrary;
    }

    public SLibrary getBase() {
        // assuming that base is the first library added
        // as seen in the constructor
        return availableLibraries.getFirst();
    }
}
