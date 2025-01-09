package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.nodes.STypesStrong;
import truffle_scheme6.runtime.numbers.SBigInt;
import truffle_scheme6.runtime.numbers.SFractionBigInt;
import truffle_scheme6.runtime.numbers.SFractionLong;

import java.math.BigInteger;

@BuiltinInfo(name = "even?")
@NodeChild(value = "arg", type = SchemeNode.class)
@TypeSystemReference(STypesStrong.class)
public abstract class SIsEven extends SBuiltin {
    @Specialization
    public boolean doLong(long l) {
        return l % 2 == 0;
    }

    @Specialization
    public boolean doBigInt(SBigInt i) {
        return i.value().remainder(BigInteger.TWO).compareTo(BigInteger.ZERO) == 0;
    }

    @Specialization(rewriteOn = ArithmeticException.class)
    public boolean doLongFraction(SFractionLong fraction) {
        var divTwo = fraction.divideExact(new SFractionLong(2));
        return divTwo.isPerfectlyDivisible();
    }

    @Specialization
    public boolean doBigFraction(SFractionBigInt fraction) {
        var divTwo = fraction.divide(new SFractionBigInt(BigInteger.TWO));
        return divTwo.isPerfectlyDivisible();
    }
}
