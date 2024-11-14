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
abstract class UBinaryAddition extends Node {
    public abstract Object execute(Object a, Object b);

    @Specialization
    static long doLongs(long a, long b) {
        return a + b;
    }

    @Specialization
    static BigInteger doBigIntegers(BigInteger a, BigInteger b) {
        return a.add(b);
    }

    @Specialization
    static SFractionLong doLongFractions(SFractionLong a, SFractionLong b) {
        return a.add(b);
    }

    @Specialization
    static SFractionBigInt doBigFractions(SFractionBigInt a, SFractionBigInt b) {
        return a.add(b);
    }

    @Specialization
    static float doFloats(float a, float b) {
        return a + b;
    }

    @Specialization
    static double doDoubles(double a, double b) {
        return a + b;
    }

    @Specialization
    static BigDecimal doBigDecimals(BigDecimal a, BigDecimal b) {
        return a.add(b);
    }

    @Specialization
    static SComplexLong doComplexLongs(SComplexLong a, SComplexLong b) {
        return new SComplexLong(a.real().add(b.real()), a.imag().add(b.imag()));
    }

    @Specialization
    static SComplexBigInt doComplexBigInts(SComplexBigInt a, SComplexBigInt b) {
        return new SComplexBigInt(a.real().add(b.real()), a.imag().add(b.imag()));
    }

    @Specialization
    static SComplexFloat doComplexFloats(SComplexFloat a, SComplexFloat b) {
        return new SComplexFloat(a.real() + b.real(), a.imag() + b.imag());
    }

    @Specialization
    static SComplexDouble doComplexDoubles(SComplexDouble a, SComplexDouble b) {
        return new SComplexDouble(a.real() + b.real(), a.imag() + b.imag());
    }

    @Specialization
    static SComplexBigDec doComplexBigDecimals(SComplexBigDec a, SComplexBigDec b) {
        return new SComplexBigDec(a.real().add(b.real()), a.imag().add(b.imag()));
    }
}
