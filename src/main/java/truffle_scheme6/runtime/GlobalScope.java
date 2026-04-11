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
