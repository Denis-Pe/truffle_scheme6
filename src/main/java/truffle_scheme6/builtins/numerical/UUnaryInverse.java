package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.GeneratePackagePrivate;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.nodes.Node;
import jdk.dynalink.beans.StaticClass;
import truffle_scheme6.nodes.STypesStrong;
import truffle_scheme6.runtime.numbers.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

@GeneratePackagePrivate
@TypeSystemReference(STypesStrong.class)
abstract class UUnaryInverse extends Node {
    public abstract Object execute(Object o);

    @Specialization(rewriteOn = ArithmeticException.class)
    static SFractionLong doLong(long l) {
        if (l == 0)
            throw new RuntimeException("Illegal argument: can't invert zero");
        return new SFractionLong(Long.signum(l), Math.absExact(l));
    }

    @Specialization
    static SFractionBigInt doBigInt(SBigInt i) {
        var val = i.value();
        if (val.compareTo(BigInteger.ZERO) == 0)
            throw new RuntimeException("Illegal argument: can't invert zero");
        return new SFractionBigInt(BigInteger.valueOf(val.signum()), val.abs());
    }

    @Specialization(rewriteOn = ArithmeticException.class)
    static SFractionLong doLongFraction(SFractionLong fraction) {
        return fraction.inverseExact();
    }

    @Specialization
    static SFractionBigInt doBigIntFraction(SFractionBigInt fraction) {
        return fraction.inverse();
    }

    @Specialization
    static SBigDec doBigDec(SBigDec d) {
        return new SBigDec(BigDecimal.ONE.divide(d.value(), MathContext.DECIMAL128));
    }

    @Specialization
    static float doFloat(float f) {
        return 1.0f / f;
    }

    @Specialization
    static double doDouble(double d) {
        return 1.0 / d;
    }

    @Specialization
    static SComplexLong doLongComplex(SComplexLong complex) {
        return complex.inverse();
    }

    @Specialization
    static SComplexBigInt doBigIntComplex(SComplexBigInt complex) {
        return complex.inverse();
    }

    @Specialization
    static SComplexBigDec doBigDecComplex(SComplexBigDec complex) {
        return complex.inverse();
    }

    @Specialization
    static SComplexFloat doFloatComplex(SComplexFloat complex) {
        return complex.inverse();
    }

    @Specialization
    static SComplexDouble doDoubleComplex(SComplexDouble complex) {
        return complex.inverse();
    }
}
