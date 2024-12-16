package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.runtime.numbers.SBigInt;
import truffle_scheme6.runtime.numbers.SFractionBigInt;
import truffle_scheme6.runtime.numbers.SFractionLong;

@BuiltinInfo(name = "numerator")
@NodeChild(value = "arg", type = SchemeNode.class)
public abstract class SNumerator extends SBuiltin {
    @Specialization(rewriteOn = ArithmeticException.class)
    public long doLongFraction(SFractionLong fraction) {
        return fraction.simplifiedExact().numerator();
    }

    @Specialization(replaces = "doLongFraction")
    public SBigInt doBigFraction(SFractionBigInt fraction) {
        return new SBigInt(fraction.simplified().numerator());
    }
    
    // todo implement this as well as (denominator q) for inexact numbers too, which means I'll have to
    //  to derive fractions from decimals in their lowest terms possible, which means that then I'll be able
    //  to replace big decimals for fractions when representing exact decimals the way Chez does. And I'll finally
    //  be able to divide exact "decimals" in peace
}
