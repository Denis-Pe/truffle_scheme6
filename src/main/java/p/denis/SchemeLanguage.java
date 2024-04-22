package p.denis;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.TruffleLanguage;

@TruffleLanguage.Registration(id = "scheme", name = "R6RS Scheme")
public final class SchemeLanguage extends TruffleLanguage<SchemeLanguageContext> {
    @Override
    protected CallTarget parse(ParsingRequest request) throws Exception {
        // todo
        request.getSource().getCharacters();
        return null;
    }

    @Override
    protected SchemeLanguageContext createContext(Env env) {
        return new SchemeLanguageContext();
    }
}
