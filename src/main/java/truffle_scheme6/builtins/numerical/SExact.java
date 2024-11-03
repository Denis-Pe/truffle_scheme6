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

@BuiltinInfo(name = "exact")
@NodeChild(value = "arg", type = SReadArgSlotNode.class)
@TypeSystemReference(STypesStrong.class)
public abstract class SExact extends SBuiltin {
    @Specialization
    public SFixnum doFixnum(SFixnum fixnum) {
        return fixnum;
    }

    @Specialization
    public SBigInt doBigInt(SBigInt bigint) {
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
        return new SComplexBigDec(BigDecimal.valueOf(complexDouble.getReal()), BigDecimal.valueOf(complexDouble.getImag()));
    }

    @Specialization
    public SComplexBigDec doComplexFloat(SComplexFloat complexFloat) {
        return new SComplexBigDec(BigDecimal.valueOf(complexFloat.getReal()), BigDecimal.valueOf(complexFloat.getImag()));
    }

    @Specialization
    public SComplexRational doComplexRational(SComplexRational complexRational) {
        return complexRational;
    }
}
