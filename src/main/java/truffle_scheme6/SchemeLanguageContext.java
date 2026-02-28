package truffle_scheme6;

import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.nodes.Node;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.builtins.*;
import truffle_scheme6.builtins.booleans.SIsBooleanFactory;
import truffle_scheme6.builtins.characters.SCharToIntegerFactory;
import truffle_scheme6.builtins.characters.SIntegerToCharFactory;
import truffle_scheme6.builtins.characters.SIsCharFactory;
import truffle_scheme6.builtins.numerical.*;
import truffle_scheme6.builtins.pairs.*;
import truffle_scheme6.builtins.procedures.SIsProcedureFactory;
import truffle_scheme6.builtins.strings.SIsStringFactory;
import truffle_scheme6.builtins.symbols.SAreSymbolsEqualFactory;
import truffle_scheme6.builtins.symbols.SIsSymbolFactory;
import truffle_scheme6.builtins.symbols.SStringToSymbolFactory;
import truffle_scheme6.builtins.symbols.SSymbolToStringFactory;
import truffle_scheme6.builtins.vectors.SIsVectorFactory;
import truffle_scheme6.nodes.functions.SReadArgNode;
import truffle_scheme6.nodes.functions.SReadArgSlotNode;
import truffle_scheme6.nodes.functions.SReadVarArgsNode;
import truffle_scheme6.nodes.roots.SLambdaRoot;
import truffle_scheme6.runtime.*;

import java.util.ArrayList;
import java.util.List;

public class SchemeLanguageContext {
    private final SchemeLanguage language;

    private final static TruffleLanguage.ContextReference<SchemeLanguageContext> REF =
            TruffleLanguage.ContextReference.create(SchemeLanguage.class);

    public static SchemeLanguageContext get(Node node) {
        return REF.get(node);
    }

    public final GlobalScope globalScope;

    // language runtime initialization
    public SchemeLanguageContext(SchemeLanguage language) {
        this.language = language;
        this.globalScope = new GlobalScope(null);
        installBase();
    }

    private void installBase() {
        var base = globalScope.getBase();

        installBaseBuiltin(SIsProcedureFactory.getInstance());

        installBaseBuiltin(SIsStringFactory.getInstance());

        installBaseBuiltin(SIsVectorFactory.getInstance());

        installBaseBuiltin(SIsCharFactory.getInstance());

        installBaseBuiltin(SIsBooleanFactory.getInstance());

        /* NUMERICAL */

        installBaseBuiltin(SIsNumberFactory.getInstance());
        installBaseBuiltin(SIsComplexFactory.getInstance());
        installBaseBuiltin(SIsRealFactory.getInstance());
        installBaseBuiltin(SIsRationalFactory.getInstance());
        installBaseBuiltin(SIsIntegerFactory.getInstance());

        installBaseBuiltin(SIsRealValuedFactory.getInstance());
        installBaseBuiltin(SIsRationalValuedFactory.getInstance());
        installBaseBuiltin(SIsIntegerValuedFactory.getInstance());

        installBaseBuiltin(SIsExactFactory.getInstance());
        installBaseBuiltin(SIsInexactFactory.getInstance());

        installBaseBuiltin(SExactFactory.getInstance());
        installBaseBuiltin(SInexactFactory.getInstance());

        installBaseBuiltin(SNumsEqualFactory.getInstance());
        installBaseBuiltin(SNumsIncreasingFactory.getInstance());
        installBaseBuiltin(SNumsDecreasingFactory.getInstance());
        installBaseBuiltin(SNumsNonDecreasingFactory.getInstance());
        installBaseBuiltin(SNumsNonIncreasingFactory.getInstance());

        installBaseBuiltin(SIsZeroFactory.getInstance());
        installBaseBuiltin(SIsPositiveFactory.getInstance());
        installBaseBuiltin(SIsNegativeFactory.getInstance());
        installBaseBuiltin(SIsOddFactory.getInstance());
        installBaseBuiltin(SIsEvenFactory.getInstance());
        installBaseBuiltin(SIsFiniteFactory.getInstance());
        installBaseBuiltin(SIsInfiniteFactory.getInstance());
        installBaseBuiltin(SIsNanFactory.getInstance());

        installBaseBuiltin(SNumsAddFactory.getInstance());
        installBaseBuiltin(SNumsMultiplyFactory.getInstance());

        installBaseBuiltin(SNumsMinusFactory.getInstance());

        installBaseBuiltin(SNumsDivFactory.getInstance());

        installBaseBuiltin(SAbsFactory.getInstance());

        installBaseBuiltin(SNumeratorFactory.getInstance());
        installBaseBuiltin(SDenominatorFactory.getInstance());

        /* PAIRS AND LISTS */

        installBaseBuiltin(SIsPairFactory.getInstance());

        installBaseBuiltin(SConsFactory.getInstance());

        installBaseBuiltin(SCarFactory.getInstance());

        installBaseBuiltin(SCdrFactory.getInstance());

        installBaseBuiltin(SIsNullFactory.getInstance());

        installBaseBuiltin(SIsListFactory.getInstance());

        installBaseBuiltin(SMakeListFactory.getInstance());

        installBaseBuiltin(SLengthFactory.getInstance());

        /* SYMBOLS */

        installBaseBuiltin(SIsSymbolFactory.getInstance());

        installBaseBuiltin(SSymbolToStringFactory.getInstance());

        installBaseBuiltin(SAreSymbolsEqualFactory.getInstance());

        installBaseBuiltin(SStringToSymbolFactory.getInstance());

        /* CHARACTERS */

        installBaseBuiltin(SIntegerToCharFactory.getInstance());

        installBaseBuiltin(SCharToIntegerFactory.getInstance());
    }

    private void installBaseBuiltin(NodeFactory<? extends SBuiltin> factory) {
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

        var funName = SSymbol.get(name);
        var funNode = factory.createNode(argReaders);
        var lambdaRoot = new SLambdaRoot(language, new FrameDescriptor(), name, funNode);
        var lambda = new SLambda(lambdaRoot.getCallTarget(),
                lastVarArgs ? numArgs - 1 : numArgs, lastVarArgs);
        if (!globalScope.setVar(funName, lambda))
            throw new IllegalStateException("Base library installation failed: name `"
                    + name + "` already bound");
        globalScope.getBase().addExport(funName, lambda); // useful later
    }
}
