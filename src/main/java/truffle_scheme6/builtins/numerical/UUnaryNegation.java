package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.GeneratePackagePrivate;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.nodes.Node;
import truffle_scheme6.nodes.STypesStrong;
import truffle_scheme6.runtime.numbers.*;

@GeneratePackagePrivate
@TypeSystemReference(STypesStrong.class)
abstract class UUnaryNegation extends Node {
    public abstract Object execute(Object value);

    @Specialization(rewriteOn = ArithmeticException.class)
    static long doLong(long l) {
        return Math.negateExact(l);
    }

    @Specialization
    static SBigInt doBigInt(SBigInt i) {
        return new SBigInt(i.value().negate());
    }

    @Specialization(rewriteOn = ArithmeticException.class)
    static SFractionLong doLongFraction(SFractionLong fraction) {
        return fraction.negateExact();
    }

    @Specialization
    static SFractionBigInt doBigFraction(SFractionBigInt fraction) {
        return fraction.negate();
    }

    @Specialization
    static SBigDec doBigDec(SBigDec bigDec) {
        return new SBigDec(bigDec.value().negate());
    }

    @Specialization
    static float doFloat(float f) {
        return -f;
    }

    @Specialization
    static double doDouble(double d) {
        return -d;
    }

    @Specialization
    static SComplexLong doLongComplex(SComplexLong complex) {
        return complex.negate();
    }

    @Specialization
    static SComplexBigInt doBigIntComplex(SComplexBigInt complex) {
        return complex.negate();
    }

    @Specialization
    static SComplexBigDec doBigDecComplex(SComplexBigDec complex) {
        return complex.negate();
    }

    @Specialization
    static SComplexFloat doFloatComplex(SComplexFloat complex) {
        return complex.negate();
    }

    @Specialization
    static SComplexDouble doDoubleComplex(SComplexDouble complex) {
        return complex.negate();
    }
}
