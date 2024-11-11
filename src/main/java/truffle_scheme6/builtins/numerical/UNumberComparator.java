package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.GeneratePackagePrivate;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.nodes.Node;
import truffle_scheme6.builtins.numerical_utils.ComparisonResult;
import truffle_scheme6.nodes.STypes;
import truffle_scheme6.runtime.numbers.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@GeneratePackagePrivate
@TypeSystemReference(STypes.class)
public abstract class UNumberComparator extends Node {
    public abstract ComparisonResult execute(Object a, Object b);

    /*--- REAL COMPARISONS ---*/

    @Specialization
    static ComparisonResult doLongs(long a, long b) {
        return ComparisonResult.from(Long.compare(a, b));
    }

    @Specialization
    static ComparisonResult doBigIntegers(BigInteger a, BigInteger b) {
        return ComparisonResult.from(a.compareTo(b));
    }

    @Specialization
    static ComparisonResult doLongFractions(SFractionLong a, SFractionLong b) {
        return ComparisonResult.from(a.compareTo(b));
    }

    @Specialization
    static ComparisonResult doBigFractions(SFractionBigInt a, SFractionBigInt b) {
        return ComparisonResult.from(a.compareTo(b));
    }

    @Specialization
    static ComparisonResult doFloats(float a, float b) {
        return ComparisonResult.from(Float.compare(a, b));
    }

    @Specialization
    static ComparisonResult doDoubles(double a, double b) {
        return ComparisonResult.from(Double.compare(a, b));
    }

    @Specialization
    static ComparisonResult doBigDecimals(BigDecimal a, BigDecimal b) {
        return ComparisonResult.from(a.compareTo(b));
    }

    /*--- COMPLEX COMPARISONS ---*/

    @Specialization
    static ComparisonResult doComplexRational(SComplexRational a, SComplexRational b) {
        // todo 
        //  I'm going to split SComplexRational into two classes but for now I want to complete this before I do
        return ComparisonResult.Equal;
    }

    @Specialization
    static ComparisonResult doComplexFloat(SComplexFloat a, SComplexFloat b) {
        if (a.real() == b.real() && a.imag() == b.imag()) {
            return ComparisonResult.Equal;
        } else {
            return ComparisonResult.Unequal;
        }
    }

    @Specialization
    static ComparisonResult doComplexDouble(SComplexDouble a, SComplexDouble b) {
        if (a.real() == b.real() && a.imag() == b.imag()) {
            return ComparisonResult.Equal;
        } else {
            return ComparisonResult.Unequal;
        }
    }

    @Specialization
    static ComparisonResult doComplexBigDec(SComplexBigDec a, SComplexBigDec b) {
        if (a.real().equals(b.real()) && a.imag().equals(b.imag())) {
            return ComparisonResult.Equal;
        } else {
            return ComparisonResult.Unequal;
        }
    }
}
