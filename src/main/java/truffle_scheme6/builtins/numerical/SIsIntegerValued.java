package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.nodes.STypesStrong;
import truffle_scheme6.nodes.functions.SReadArgSlotNode;
import truffle_scheme6.runtime.numbers.*;

@BuiltinInfo(name = "integer-valued?")
@NodeChild(value = "arg", type = SReadArgSlotNode.class)
@TypeSystemReference(STypesStrong.class)
public abstract class SIsIntegerValued extends SBuiltin {
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
        var nonComplex = arg instanceof SBigInt
                || arg instanceof SFixnum
                || (arg instanceof SFractionBigInt fractionBig && fractionBig.isPerfectlyDivisible())
                || (arg instanceof SFractionLong fractionLong && fractionLong.isPerfectlyDivisible());

        return nonComplex || arg instanceof SComplexRational complexRational && complexRational.getImag().isZero() && (
                complexRational.getImag() instanceof SFixnum
                        || complexRational.getImag() instanceof SBigInt
                        || (complexRational.getImag() instanceof SFractionBigInt fractionBig && fractionBig.isPerfectlyDivisible())
                        || (complexRational.getImag() instanceof SFractionLong fractionLong && fractionLong.isPerfectlyDivisible())
        );
    }
}
