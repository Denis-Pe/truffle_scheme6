package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.GeneratePackagePrivate;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.nodes.Node;
import truffle_scheme6.nodes.STypes;
import truffle_scheme6.runtime.numbers.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@GeneratePackagePrivate
@TypeSystemReference(STypes.class)
abstract class UBinaryMultiplication extends Node {
    public abstract Object execute(Object a, Object b);

    @Specialization
    static long doLongs(long a, long b) {
        return a + b;
    }

    @Specialization
    static BigInteger doBigIntegers(BigInteger a, BigInteger b) {
        return a.multiply(b);
    }

    @Specialization
    static SFractionLong doLongFractions(SFractionLong a, SFractionLong b) {
        return a.multiply(b);
    }

    @Specialization
    static SFractionBigInt doBigFractions(SFractionBigInt a, SFractionBigInt b) {
        return a.multiply(b);
    }

    @Specialization
    static BigDecimal doBigDecimals(BigDecimal a, BigDecimal b) {
        return a.multiply(b);
    }

    @Specialization
    static float doFloats(float a, float b) {
        return a * b;
    }

    @Specialization
    static double doDoubles(double a, double b) {
        return a * b;
    }

    @Specialization
    static SComplexLong doComplexLongs(SComplexLong a, SComplexLong b) {
        var newReal = a.real().multiply(b.real())
                .subtract(a.imag().multiply(b.imag()));
        var newImag = a.real().multiply(b.imag()).add(a.imag().multiply(b.real()));
        return new SComplexLong(newReal, newImag);
    }

    @Specialization
    static SComplexBigInt doComplexBigInts(SComplexBigInt a, SComplexBigInt b) {
        var newReal = a.real().multiply(b.real())
                .subtract(a.imag().multiply(b.imag()));
        var newImag = a.real().multiply(b.imag()).add(a.imag().multiply(b.real()));
        return new SComplexBigInt(newReal, newImag);
    }

    @Specialization
    static SComplexBigDec doComplexBigDecimals(SComplexBigDec a, SComplexBigDec b) {
        var newReal = a.real().multiply(b.real())
                .subtract(a.imag().multiply(b.imag()));
        var newImag = a.real().multiply(b.imag()).add(a.imag().multiply(b.real()));
        return new SComplexBigDec(newReal, newImag);
    }

    @Specialization
    static SComplexFloat doComplexFloats(SComplexFloat a, SComplexFloat b) {
        var newReal = a.real() * b.real() - a.imag() * b.imag();
        var newImag = a.real() * b.imag() + a.imag() * b.real();
        return new SComplexFloat(newReal, newImag);
    }

    @Specialization
    static SComplexDouble doComplexDoubles(SComplexDouble a, SComplexDouble b) {
        var newReal = a.real() * b.real() - a.imag() * b.imag();
        var newImag = a.real() * b.imag() + a.imag() * b.real();
        return new SComplexDouble(newReal, newImag);
    }
}
