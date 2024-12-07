package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.runtime.numbers.SBigInt;
import truffle_scheme6.runtime.numbers.SFractionBigInt;
import truffle_scheme6.runtime.numbers.SFractionLong;

@BuiltinInfo(name = "denominator")
@NodeChild(value = "arg", type = SchemeNode.class)
public abstract class SDenominator extends SBuiltin {
    @Specialization(rewriteOn = ArithmeticException.class)
    public long doLongFraction(SFractionLong fraction) {
        return fraction.simplifiedExact().denominator();
    }

    @Specialization(replaces = "doLongFraction")
    public SBigInt doBigFraction(SFractionBigInt fraction) {
        return new SBigInt(fraction.simplified().denominator());
    }
}
