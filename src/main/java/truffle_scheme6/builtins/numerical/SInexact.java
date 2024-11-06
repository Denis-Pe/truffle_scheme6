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
import java.math.BigInteger;

@BuiltinInfo(name = "inexact?")
@NodeChild(value = "arg", type = SReadArgSlotNode.class)
@TypeSystemReference(STypesStrong.class)
public abstract class SInexact extends SBuiltin {
    @Specialization
    public double doLong(long fixnum) {
        return fixnum;
    }

    @Specialization
    public double doBigInteger(BigInteger bigint) {
        return bigint.doubleValue();
    }

    @Specialization
    public double doFractionLong(SFractionLong fractionLong) {
        return ((double) fractionLong.numerator()) / ((double) fractionLong.denominator());
    }

    @Specialization
    public double doFractionBigInt(SFractionBigInt fractionBigInt) {
        return fractionBigInt.numerator().doubleValue() / fractionBigInt.denominator().doubleValue();
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
                complexBigDec.real().doubleValue(),
                complexBigDec.imag().doubleValue()
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
                complexRational.real().doubleValue(), complexRational.imag().doubleValue()
        );
    }
}

