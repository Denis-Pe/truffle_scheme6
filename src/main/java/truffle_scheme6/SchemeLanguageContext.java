package truffle_scheme6;

import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.nodes.Node;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.builtins.*;
import truffle_scheme6.builtins.booleans.SIsBooleanFactory;
import truffle_scheme6.builtins.characters.SIsCharFactory;
import truffle_scheme6.builtins.numerical.*;
import truffle_scheme6.builtins.pairs.*;
import truffle_scheme6.builtins.symbols.SIsSymbolFactory;
import truffle_scheme6.builtins.vectors.SIsVectorFactory;
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
        installBuiltin(SIsVectorFactory.getInstance());

        installBuiltin(SIsCharFactory.getInstance());

        installBuiltin(SIsBooleanFactory.getInstance());

        installBuiltin(SIsSymbolFactory.getInstance());

        /* NUMERICAL */

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
        installBuiltin(SNumsDecreasingFactory.getInstance());
        installBuiltin(SNumsNonDecreasingFactory.getInstance());
        installBuiltin(SNumsNonIncreasingFactory.getInstance());

        installBuiltin(SIsZeroFactory.getInstance());
        installBuiltin(SIsPositiveFactory.getInstance());
        installBuiltin(SIsNegativeFactory.getInstance());
        installBuiltin(SIsOddFactory.getInstance());
        installBuiltin(SIsEvenFactory.getInstance());
        installBuiltin(SIsFiniteFactory.getInstance());
        installBuiltin(SIsInfiniteFactory.getInstance());
        installBuiltin(SIsNanFactory.getInstance());

        installBuiltin(SNumsAddFactory.getInstance());
        installBuiltin(SNumsMultiplyFactory.getInstance());

        installBuiltin(SNumsMinusFactory.getInstance());

        installBuiltin(SNumsDivFactory.getInstance());

        installBuiltin(SAbsFactory.getInstance());
        
        installBuiltin(SNumeratorFactory.getInstance());
        installBuiltin(SDenominatorFactory.getInstance());

        /* PAIRS AND LISTS */

        installBuiltin(SIsPairFactory.getInstance());

        installBuiltin(SConsFactory.getInstance());

        installBuiltin(SCarFactory.getInstance());

        installBuiltin(SCdrFactory.getInstance());

        installBuiltin(SIsNullFactory.getInstance());

        installBuiltin(SIsListFactory.getInstance());

        installBuiltin(SListFactory.getInstance());

        installBuiltin(SLengthFactory.getInstance());
    }

    private void installBuiltin(NodeFactory<? extends SBuiltin> factory) {
        var info = factory.getNodeClass().getAnnotation(BuiltinInfo.class);
        var name = info.name();
        var lastVarArgs = info.lastVarArgs();

        var numArgs = factory.getExecutionSignature().size();
        Object[] argReaders = new SReadArgNode[numArgs];
        for (int i = 0; i < numArgs; i++) {
            var isLast = i == numArgs - 1;

            if (isLast && lastVarArgs) {
                argReaders[i] = new SReadVarArgsNode(i);
            } else {
                argReaders[i] = new SReadArgSlotNode(i);
            }
        }

        var funNode = factory.createNode(argReaders);
        var lambdaRoot = new SLambdaRoot(language, new FrameDescriptor(), name, funNode);
        if (!this.globalScope.setVar(SSymbol.get(name), new SLambda(lambdaRoot.getCallTarget(), lastVarArgs ? numArgs - 1 : numArgs, lastVarArgs)))
            throw new IllegalStateException("Base library installation failed: name `" + name + "` already bound");
    }
}
