package truffle_scheme6.runtime;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

// todo
//  when working on macros, consider that the generated code may reference
//  a private variable. The standard allows this (see 7.1)
// todo
//  when I get to implementing the syntax for making libraries,
//  I should make their actual instantiation and evaluation as lazy as possible
//  due to the nature of dependencies and making sure a library exists,
//  it exports what other libraries want to import, etc
public class SLibrary {
    private final SPair name;

    private final Map<SSymbol, Object> exports = new IdentityHashMap<>();

    // for imports, I do not yet find it necessary to include metadata about
    // where they come from. Might change in the future
    private final Map<SSymbol, Object> runImports = new IdentityHashMap<>();
    private final Map<SSymbol, Object> expandImports = new IdentityHashMap<>();

    private final Map<SSymbol, Object> definitions = new IdentityHashMap<>();

    public SLibrary(SPair name) {
        this.name = name;
    }

    /**
     * @return true if the variable is newly bound
     */
    public boolean addExport(SSymbol identifier, Object value) {
        return exports.put(identifier, value) == null;
    }

    public Object getExport(SSymbol identifier) {
        return exports.get(identifier);
    }
}
