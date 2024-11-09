package truffle_scheme6;

import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.nodes.Node;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.builtins.*;
import truffle_scheme6.builtins.numerical.*;
import truffle_scheme6.nodes.functions.SReadArgNode;
import truffle_scheme6.nodes.functions.SReadArgSlotNode;
import truffle_scheme6.nodes.functions.SReadVarArgsNode;
import truffle_scheme6.nodes.roots.SLambdaRoot;
import truffle_scheme6.runtime.GlobalScope;
import truffle_scheme6.runtime.SLambda;
import truffle_scheme6.runtime.SSymbol;

public class SchemeLanguageContext {
    private final SchemeLanguage language;

    private final static TruffleLanguage.ContextReference<SchemeLanguageContext> REF =
            TruffleLanguage.ContextReference.create(SchemeLanguage.class);

    public static SchemeLanguageContext get(Node node) {
        return REF.get(node);
    }

    public final GlobalScope globalScope;

    public SchemeLanguageContext(SchemeLanguage language) {
        this.language = language;
        this.globalScope = new GlobalScope();
        installBuiltins();
    }

    private void installBuiltins() {
        installBuiltin(SIsSymbolFactory.getInstance());
        
        installBuiltin(SIsNumberFactory.getInstance());
        installBuiltin(SIsComplexFactory.getInstance());
        installBuiltin(SIsRealFactory.getInstance());
        installBuiltin(SIsRationalFactory.getInstance());
        installBuiltin(SIsIntegerFactory.getInstance());
        
        installBuiltin(SIsRealValuedFactory.getInstance());
        installBuiltin(SIsRationalValuedFactory.getInstance());
        installBuiltin(SIsIntegerValuedFactory.getInstance());
        
        installBuiltin(SIsExactFactory.getInstance());
        installBuiltin(SIsInexactFactory.getInstance());
        
        installBuiltin(SExactFactory.getInstance());
        installBuiltin(SInexactFactory.getInstance());
        
        installBuiltin(SNumsEqualFactory.getInstance());
        installBuiltin(SNumsIncreasingFactory.getInstance());
    }

    private void installBuiltin(NodeFactory<? extends SBuiltin> factory) {
        var info = factory.getNodeClass().getAnnotation(BuiltinInfo.class);
        var name = info.name();
        var lastVarArgs = info.lastVarArgs();

        var numArgs = factory.getExecutionSignature().size();
        var argReaders = new SReadArgNode[numArgs];
        for (int i = 0; i < numArgs; i++) {
            var isLast = i == numArgs - 1;

            if (isLast && lastVarArgs) {
                argReaders[i] = new SReadVarArgsNode(i);
            } else {
                argReaders[i] = new SReadArgSlotNode(i);
            }
        }

        var funNode = factory.createNode((Object[]) argReaders);
        var lambdaRoot = new SLambdaRoot(language, new FrameDescriptor(), name, funNode);
        this.globalScope.setVar(SSymbol.get(name), new SLambda(lambdaRoot.getCallTarget()));
    }
}
