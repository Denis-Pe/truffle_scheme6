package truffle_scheme6.runtime;

import truffle_scheme6.SchemeLanguageContext;

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
//  in reality, it is part of a broader problem in knowing what should be loaded first,
//  or perhaps that problem could be bypassed by laziness
public class SLibrary {
    private static class Definition {
        public boolean isExported = false;
        public boolean isRunImport = false;
        public boolean isExpandImport = false;
        public SLibrary origin;
        public Object value = null;

        public Definition(SLibrary origin) {
            this.origin = origin;
        }

        public Definition(SLibrary origin, Object value) {
            this.origin = origin;
            this.value = value;
        }

        public boolean isBound() {
            return value != null;
        }
    }

    private final SPair name;

    private final Map<SSymbol, Definition> definitions = new IdentityHashMap<>();

    public SLibrary(SPair name) {
        this.name = name;
    }

    public void importFrom(boolean shouldReExport, boolean isRunImport, boolean isExpandImport, SLibrary origin, SSymbol... symbols) {
        for (var s : symbols) {
            var libDef = origin.requestDefinition(s, this);

            if (libDef == null) {
                throw new IllegalStateException("Cannot find definition for `%s`".formatted(s));
            }

            var def = new Definition(origin, libDef.value);
            def.isExported = shouldReExport;
            def.isRunImport = isRunImport;
            def.isExpandImport = isExpandImport;
            definitions.put(s, def);
        }
    }

    /**
     * Makes a new definition from within this library.
     * Can also bind a previously unbound definition of the library.
     *
     * If the identifier was present, the {@code isExported} argument
     * will be ignored.
     *
     * @return true if the variable is newly bound
     */
    public boolean addDefinition(SSymbol identifier, Object value, boolean isExported) {
        var def = definitions.get(identifier);

        if (def == null) {
            def = new Definition(this);
            definitions.put(identifier, def);
            def.isExported = isExported;
            return true;
        } else {
            def.value = value;
            return false;
        }
    }

    /**
     * Makes a new definition within this library. <b>Not</b> exported by default.
     *
     * @return true if the variable is newly bound
     */
    public boolean addDefinition(SSymbol identifier, Object value) {
        return addDefinition(identifier, value, false);
    }

    private Definition requestDefinition(SSymbol identifier, SLibrary requestSource) {
        var def = definitions.get(identifier);
        if (def == null || !def.isBound()) {
            return null;
        }

        if (def.isExported || requestSource == this) {
            return def;
        } else {
            throw new UnsupportedOperationException("%s is an internal definition %s does not have access to.".formatted(identifier, requestSource));
        }
    }

    public Object getDefinition(SSymbol identifier, SLibrary requestSource) {
        var d = requestDefinition(identifier, requestSource);

        if (d == null) {
            return null;
        } else {
            return d.value;
        }
    }

    public Object getDefinition(SSymbol identifier) {
        return getDefinition(identifier, SchemeLanguageContext.get(null).globalScope.getActiveLibrary());
    }
}
