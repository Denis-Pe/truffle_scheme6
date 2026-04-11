package truffle_scheme6.runtime;

import java.util.*;

public final class GlobalScope {
    private final Map<SSymbol, Object> vars = new IdentityHashMap<>();
    private SLibrary activeLibrary;
    public final List<SLibrary> availableLibraries = new ArrayList<>();

    public GlobalScope() {
        var base = new SLibrary(SPair.list(
                SSymbol.get("rnrs"),
                SSymbol.get("base"),
                SList.list(6)));
        availableLibraries.add(base);
        this.activeLibrary = base;
    }

    public GlobalScope(SLibrary activeLibrary) {
        this();
        Objects.requireNonNull(activeLibrary);
        this.activeLibrary = activeLibrary;
    }

//    /**
//     * @param name name of the variable
//     * @param val  value of the variable
//     * @return true if the variable is new. false if it is changing the value of an existing variable
//     */
//    public boolean setVar(SSymbol name, Object val) {
//        return vars.put(name, val) == null;
//    }
//
//    public Object getVar(SSymbol name) {
//        return activeLibrary.getExport(name);
//    }

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
