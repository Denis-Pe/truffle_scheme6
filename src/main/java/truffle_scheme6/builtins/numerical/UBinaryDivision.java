package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.GeneratePackagePrivate;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.nodes.Node;
import truffle_scheme6.nodes.STypes;
import truffle_scheme6.runtime.numbers.*;

import java.math.BigInteger;
import java.math.MathContext;

@GeneratePackagePrivate
@TypeSystemReference(STypes.class)
abstract class UBinaryDivision extends Node {
    public abstract Object execute(Object a, Object b);

    @Specialization(rewriteOn = ArithmeticException.class)
    static SFractionLong doLongs(long a, long b) {
        long signum = (long) Long.signum(a) * Long.signum(b);
        return new SFractionLong(Math.absExact(a) * signum, Math.absExact(b));
    }

    @Specialization
    static SFractionBigInt doBigInts(SBigInt a, SBigInt b) {
        BigInteger signum = BigInteger.valueOf(a.value().signum() * b.value().signum());
        return new SFractionBigInt(a.value().abs().multiply(signum), b.value().abs());
    }

    @Specialization(rewriteOn = ArithmeticException.class)
    static SFractionLong doLongFractions(SFractionLong a, SFractionLong b) {
        return a.divideExact(b);
    }

    @Specialization
    static SFractionBigInt doBigFractions(SFractionBigInt a, SFractionBigInt b) {
        return a.divide(b);
    }

    @Specialization
    static SBigDec doBigDecs(SBigDec a, SBigDec b) {
        return new SBigDec(a.value().divide(b.value(), MathContext.DECIMAL128));
    }

    @Specialization
    static float doFloats(float a, float b) {
        return a / b;
    }

    @Specialization
    static double doDoubles(double a, double b) {
        return a / b;
    }

    @Specialization
    static SComplexLong doLongComplexes(SComplexLong a, SComplexLong b) {
        return a.divide(b);
    }

    @Specialization
    static SComplexBigInt doBigIntComplexes(SComplexBigInt a, SComplexBigInt b) {
        return a.divide(b);
    }

    @Specialization
    static SComplexBigDec doBigDecComplexes(SComplexBigDec a, SComplexBigDec b) {
        return a.divide(b);
    }

    @Specialization
    static SComplexFloat doFloatComplexes(SComplexFloat a, SComplexFloat b) {
        return a.divide(b);
    }

    @Specialization
    static SComplexDouble doDoubleComplexes(SComplexDouble a, SComplexDouble b) {
        return a.divide(b);
    }
}
