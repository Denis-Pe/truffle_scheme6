package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.nodes.STypesStrong;
import truffle_scheme6.nodes.functions.SReadArgSlotNode;
import truffle_scheme6.runtime.numbers.SBigInt;
import truffle_scheme6.runtime.numbers.SFixnum;
import truffle_scheme6.runtime.numbers.SFractionBigInt;
import truffle_scheme6.runtime.numbers.SFractionLong;

@BuiltinInfo(name = "integer?")
@NodeChild(value = "arg", type = SReadArgSlotNode.class)
@TypeSystemReference(STypesStrong.class)
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
                || (arg instanceof SFractionBigInt fractionBig && fractionBig.isPerfectlyDivisible())
                || (arg instanceof SFractionLong fractionLong && fractionLong.isPerfectlyDivisible());
    }
}
