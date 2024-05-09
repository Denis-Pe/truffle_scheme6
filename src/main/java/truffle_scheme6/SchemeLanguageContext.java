package truffle_scheme6;

import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.nodes.Node;
import truffle_scheme6.runtime.GlobalScope;

public class SchemeLanguageContext {
    private final static TruffleLanguage.ContextReference<SchemeLanguageContext> REF =
            TruffleLanguage.ContextReference.create(SchemeLanguage.class);

    public static SchemeLanguageContext get(Node node) {
        return REF.get(node);
    }

    public final GlobalScope globalScope;

    public SchemeLanguageContext() {
        this.globalScope = new GlobalScope();
    }
}
