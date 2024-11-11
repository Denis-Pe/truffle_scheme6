package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.GeneratePackagePrivate;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.nodes.Node;
import truffle_scheme6.nodes.STypes;
import truffle_scheme6.runtime.numbers.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.Set;

/**
 * Utility node for number comparisons.
 * Package private
 */
@GeneratePackagePrivate
@TypeSystemReference(STypes.class)
abstract class UNumberComparator extends Node {
    public abstract UComparisonResult execute(Object a, Object b);

    /*--- REAL COMPARISONS ---*/

    @Specialization
    static UComparisonResult doLongs(long a, long b) {
        return UComparisonResult.from(Long.compare(a, b));
    }

    @Specialization
    static UComparisonResult doBigIntegers(BigInteger a, BigInteger b) {
        return UComparisonResult.from(a.compareTo(b));
    }

    @Specialization
    static UComparisonResult doLongFractions(SFractionLong a, SFractionLong b) {
        return UComparisonResult.from(a.compareTo(b));
    }

    @Specialization
    static UComparisonResult doBigFractions(SFractionBigInt a, SFractionBigInt b) {
        return UComparisonResult.from(a.compareTo(b));
    }

    @Specialization
    static UComparisonResult doFloats(float a, float b) {
        return UComparisonResult.from(Float.compare(a, b));
    }

    @Specialization
    static UComparisonResult doDoubles(double a, double b) {
        return UComparisonResult.from(Double.compare(a, b));
    }

    @Specialization
    static UComparisonResult doBigDecimals(BigDecimal a, BigDecimal b) {
        return UComparisonResult.from(a.compareTo(b));
    }

    /*--- COMPLEX COMPARISONS ---*/

    @Specialization
    static UComparisonResult doComplexLongs(SComplexLong a, SComplexLong b) {
        if (a.real().equals(b.real()) && a.imag().equals(b.imag())) {
            return UComparisonResult.Equal;
        } else {
            return UComparisonResult.Unequal;
        }
    }

    @Specialization
    static UComparisonResult doComplexBigInts(SComplexBigInt a, SComplexBigInt b) {
        if (a.real().equals(b.real()) && a.imag().equals(b.imag())) {
            return UComparisonResult.Equal;
        } else {
            return UComparisonResult.Unequal;
        }
    }

    @Specialization
    static UComparisonResult doComplexFloat(SComplexFloat a, SComplexFloat b) {
        if (a.real() == b.real() && a.imag() == b.imag()) {
            return UComparisonResult.Equal;
        } else {
            return UComparisonResult.Unequal;
        }
    }

    @Specialization
    static UComparisonResult doComplexDouble(SComplexDouble a, SComplexDouble b) {
        if (a.real() == b.real() && a.imag() == b.imag()) {
            return UComparisonResult.Equal;
        } else {
            return UComparisonResult.Unequal;
        }
    }

    @Specialization
    static UComparisonResult doComplexBigDec(SComplexBigDec a, SComplexBigDec b) {
        if (a.real().equals(b.real()) && a.imag().equals(b.imag())) {
            return UComparisonResult.Equal;
        } else {
            return UComparisonResult.Unequal;
        }
    }
}
