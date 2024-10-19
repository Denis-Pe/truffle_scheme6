package truffle_scheme6;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.nodes.Node;
import org.graalvm.polyglot.Language;
import truffle_scheme6.builtins.SIsSymbolNodeGen;
import truffle_scheme6.nodes.functions.SReadArgSlotNode;
import truffle_scheme6.nodes.roots.SLambdaRoot;
import truffle_scheme6.runtime.SLambda;
import truffle_scheme6.runtime.SSymbol;

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
        var ctx = new SchemeLanguageContext();

        var isSymbol = new SLambdaRoot(this, FrameDescriptor.newBuilder().build(), SIsSymbolNodeGen.create(new SReadArgSlotNode(0)));
        ctx.globalScope.setVar(SSymbol.get("symbol?"), new SLambda(isSymbol.getCallTarget()));

        return ctx;
    }
}
