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

@BuiltinInfo(name = "exact")
@NodeChild(value = "arg", type = SReadArgSlotNode.class)
@TypeSystemReference(STypesStrong.class)
public abstract class SExact extends SBuiltin {
    @Specialization
    public long doLong(long fixnum) {
        return fixnum;
    }

    @Specialization
    public BigInteger doBigInteger(BigInteger bigint) {
        return bigint;
    }

    @Specialization
    public SFractionLong doFractionLong(SFractionLong fractionLong) {
        return fractionLong;
    }

    @Specialization
    public SFractionBigInt doFractionBigInt(SFractionBigInt fractionBigInt) {
        return fractionBigInt;
    }

    @Specialization
    public BigDecimal doFloat(float f) {
        return new BigDecimal(f);
    }

    @Specialization
    public BigDecimal doDouble(double d) {
        return new BigDecimal(d);
    }

    @Specialization
    public BigDecimal doBigDecimal(BigDecimal bigDecimal) {
        return bigDecimal;
    }

    @Specialization
    public SComplexBigDec doComplexBigDec(SComplexBigDec complexBigDec) {
        return complexBigDec;
    }

    @Specialization
    public SComplexBigDec doComplexDouble(SComplexDouble complexDouble) {
        return new SComplexBigDec(BigDecimal.valueOf(complexDouble.real()), BigDecimal.valueOf(complexDouble.imag()));
    }

    @Specialization
    public SComplexBigDec doComplexFloat(SComplexFloat complexFloat) {
        return new SComplexBigDec(BigDecimal.valueOf(complexFloat.real()), BigDecimal.valueOf(complexFloat.imag()));
    }

    @Specialization
    public SComplexRational doComplexRational(SComplexRational complexRational) {
        return complexRational;
    }
}
