package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.GeneratePackagePrivate;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.nodes.Node;
import truffle_scheme6.nodes.STypes;
import truffle_scheme6.runtime.numbers.*;

@GeneratePackagePrivate
@TypeSystemReference(STypes.class)
abstract class UBinaryAddition extends Node {
    public abstract Object execute(Object a, Object b);

    @Specialization(rewriteOn = ArithmeticException.class)
    static long doLongs(long a, long b) {
        return Math.addExact(a, b);
    }

    @Specialization
    static SBigInt doBigIntegers(SBigInt a, SBigInt b) {
        return new SBigInt(a.value().add(b.value()));
    }

    @Specialization(rewriteOn = ArithmeticException.class)
    static SFractionLong doLongFractions(SFractionLong a, SFractionLong b) {
        return a.addExact(b);
    }

    @Specialization
    static SFractionBigInt doBigFractions(SFractionBigInt a, SFractionBigInt b) {
        return a.add(b);
    }

    @Specialization
    static SBigDec doBigDecimals(SBigDec a, SBigDec b) {
        return new SBigDec(a.value().add(b.value()));
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
    static SComplexLong doComplexLongs(SComplexLong a, SComplexLong b) {
        return a.add(b);
    }

    @Specialization
    static SComplexBigInt doComplexBigInts(SComplexBigInt a, SComplexBigInt b) {
        return a.add(b);
    }

    @Specialization
    static SComplexBigDec doComplexBigDecimals(SComplexBigDec a, SComplexBigDec b) {
        return a.add(b);
    }

    @Specialization
    static SComplexFloat doComplexFloats(SComplexFloat a, SComplexFloat b) {
        return a.add(b);
    }

    @Specialization
    static SComplexDouble doComplexDoubles(SComplexDouble a, SComplexDouble b) {
        return a.add(b);
    }
}
