package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.GeneratePackagePrivate;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.nodes.Node;
import truffle_scheme6.nodes.STypes;
import truffle_scheme6.runtime.numbers.*;

@GeneratePackagePrivate
@TypeSystemReference(STypes.class)
abstract class UBinarySubtraction extends Node {
    public abstract Object execute(Object a, Object b);

    @Specialization(rewriteOn = ArithmeticException.class)
    static long doLongs(long a, long b) {
        return Math.subtractExact(a, b);
    }

    @Specialization
    static SBigInt doBigInts(SBigInt a, SBigInt b) {
        return new SBigInt(a.value().subtract(b.value()));
    }

    @Specialization(rewriteOn = ArithmeticException.class)
    static SFractionLong doLongFractions(SFractionLong a, SFractionLong b) {
        return a.subtractExact(b);
    }

    @Specialization
    static SFractionBigInt doBigFractions(SFractionBigInt a, SFractionBigInt b) {
        return a.subtract(b);
    }

    @Specialization
    static SBigDec doBigDecs(SBigDec a, SBigDec b) {
        return new SBigDec(a.value().subtract(b.value()));
    }

    @Specialization
    static float doFloats(float a, float b) {
        return a - b;
    }

    @Specialization
    static double doDoubles(double a, double b) {
        return a - b;
    }

    @Specialization(rewriteOn = ArithmeticException.class)
    static SComplexLong doLongComplexes(SComplexLong a, SComplexLong b) {
        return a.subtractExact(b);
    }

    @Specialization
    static SComplexBigInt doBigIntComplexes(SComplexBigInt a, SComplexBigInt b) {
        return a.subtract(b);
    }

    @Specialization
    static SComplexBigDec doBigDecComplexes(SComplexBigDec a, SComplexBigDec b) {
        return a.subtract(b);
    }

    @Specialization
    static SComplexFloat doFloatComplexes(SComplexFloat a, SComplexFloat b) {
        return a.subtract(b);
    }

    @Specialization
    static SComplexDouble doDoubleComplexes(SComplexDouble a, SComplexDouble b) {
        return a.subtract(b);
    }
}
