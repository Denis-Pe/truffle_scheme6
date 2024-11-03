package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.nodes.STypesStrong;
import truffle_scheme6.nodes.functions.SReadArgSlotNode;
import truffle_scheme6.runtime.numbers.*;

import java.math.BigDecimal;

@BuiltinInfo(name = "inexact?")
@NodeChild(value = "arg", type = SReadArgSlotNode.class)
@TypeSystemReference(STypesStrong.class)
public abstract class SInexact extends SBuiltin {
    @Specialization
    public double doFixnum(SFixnum fixnum) {
        return (double) fixnum.getValue();
    }

    @Specialization
    public double doBigInt(SBigInt bigint) {
        return bigint.getValue().doubleValue();
    }

    @Specialization
    public double doFractionLong(SFractionLong fractionLong) {
        return ((double) fractionLong.getNumerator()) / ((double) fractionLong.getDenominator());
    }

    @Specialization
    public double doFractionBigInt(SFractionBigInt fractionBigInt) {
        return fractionBigInt.getNumerator().doubleValue() / fractionBigInt.getDenominator().doubleValue();
    }

    @Specialization
    public float doFloat(float f) {
        return f;
    }

    @Specialization
    public double doDouble(double d) {
        return d;
    }

    @Specialization
    public double doBigDecimal(BigDecimal bigDecimal) {
        return bigDecimal.doubleValue();
    }

    @Specialization
    public SComplexDouble doComplexBigDec(SComplexBigDec complexBigDec) {
        return new SComplexDouble(
                complexBigDec.getReal().doubleValue(),
                complexBigDec.getImag().doubleValue()
        );
    }

    @Specialization
    public SComplexDouble doComplexDouble(SComplexDouble complexDouble) {
        return complexDouble;
    }

    @Specialization
    public SComplexFloat doComplexFloat(SComplexFloat complexFloat) {
        return complexFloat;
    }

    @Specialization
    public SComplexDouble doComplexRational(SComplexRational complexRational) {
        return new SComplexDouble(
                complexRational.getReal().doubleValue(), complexRational.getImag().doubleValue()
        );
    }
}

