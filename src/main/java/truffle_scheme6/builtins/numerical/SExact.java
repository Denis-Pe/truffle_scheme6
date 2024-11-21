package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.nodes.STypesStrong;
import truffle_scheme6.runtime.numbers.*;

import java.math.BigDecimal;

@BuiltinInfo(name = "exact")
@NodeChild(value = "arg", type = SchemeNode.class)
@TypeSystemReference(STypesStrong.class)
public abstract class SExact extends SBuiltin {
    @Specialization
    public long doLong(long fixnum) {
        return fixnum;
    }

    @Specialization
    public SBigInt doBigInt(SBigInt bigint) {
        return bigint;
    }

    @Specialization
    public SFractionLong doFractionLong(SFractionLong fraction) {
        return fraction;
    }

    @Specialization
    public SFractionBigInt doFractionBigInt(SFractionBigInt fraction) {
        return fraction;
    }

    @Specialization
    public SBigDec doFloat(float f) {
        return new SBigDec(f);
    }

    @Specialization
    public SBigDec doDouble(double d) {
        return new SBigDec(d);
    }

    @Specialization
    public SBigDec doBigDecimal(SBigDec bigDecimal) {
        return bigDecimal;
    }

    @Specialization
    public SComplexBigDec doComplexBigDec(SComplexBigDec complex) {
        return complex;
    }

    @Specialization
    public SComplexBigDec doComplexDouble(SComplexDouble complex) {
        return new SComplexBigDec(BigDecimal.valueOf(complex.real()), BigDecimal.valueOf(complex.imag()));
    }

    @Specialization
    public SComplexBigDec doComplexFloat(SComplexFloat complex) {
        return new SComplexBigDec(BigDecimal.valueOf(complex.real()), BigDecimal.valueOf(complex.imag()));
    }

    @Specialization
    public SComplexBigInt doComplexBigInt(SComplexBigInt complex) {
        return complex;
    }

    @Specialization
    public SComplexLong doComplexLong(SComplexLong complex) {
        return complex;
    }
}
