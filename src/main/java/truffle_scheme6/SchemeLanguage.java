package truffle_scheme6;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.nodes.Node;

@TruffleLanguage.Registration(id = "scheme", name = "R6RS Scheme")
public final class SchemeLanguage extends TruffleLanguage<SchemeLanguageContext> {
    private static final LanguageReference<SchemeLanguage> LANGUAGE_REFERENCE = LanguageReference.create(SchemeLanguage.class);

    public static SchemeLanguage get(Node node) {
        return LANGUAGE_REFERENCE.get(node);
    }

    @Override
    protected CallTarget parse(ParsingRequest request) {
        return SchemeParser.clojureImpl.parseRoot(this, request.getSource().getCharacters())
                .getCallTarget();
    }

    @Override
    protected SchemeLanguageContext createContext(Env env) {
        return new SchemeLanguageContext(this);
    }
}
