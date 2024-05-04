package truffle_scheme6;

import truffle_scheme6.runtime.GlobalScope;

public class SchemeLanguageContext {
    public final GlobalScope globalScope;

    public SchemeLanguageContext() {
        this.globalScope = new GlobalScope();
    }
}
