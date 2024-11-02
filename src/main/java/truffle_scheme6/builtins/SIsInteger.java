package truffle_scheme6.builtins;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.nodes.functions.SReadArgSlotNode;
import truffle_scheme6.runtime.numbers.SBigInt;
import truffle_scheme6.runtime.numbers.SFixnum;
import truffle_scheme6.runtime.numbers.SFractionBigInt;
import truffle_scheme6.runtime.numbers.SFractionLong;

import java.math.BigInteger;

@BuiltinInfo(name = "integer?")
@NodeChild(value = "arg", type = SReadArgSlotNode.class)
public abstract class SIsInteger extends SBuiltin {
    @Specialization
    public boolean doFloat(float _f) {
        return false;
    }

    @Specialization
    public boolean doDouble(double _d) {
        return false;
    }

    @Specialization
    public boolean doObject(Object arg) {
        return arg instanceof SBigInt
                || arg instanceof SFixnum
                || (arg instanceof SFractionBigInt fractionBig && fractionBig.getNumerator().remainder(fractionBig.getDenominator()).equals(BigInteger.ZERO))
                || (arg instanceof SFractionLong fractionLong && fractionLong.getNumerator() % fractionLong.getDenominator() == 0);
    }
}
