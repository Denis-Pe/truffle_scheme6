package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.GeneratePackagePrivate;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.nodes.Node;
import truffle_scheme6.nodes.STypes;
import truffle_scheme6.runtime.numbers.SBigDec;
import truffle_scheme6.runtime.numbers.SBigInt;
import truffle_scheme6.runtime.numbers.SFractionBigInt;
import truffle_scheme6.runtime.numbers.SFractionLong;

/**
 * Utility node for number comparisons.
 * Package private
 */
@GeneratePackagePrivate
@TypeSystemReference(STypes.class)
abstract class URealComparator extends Node {
    public abstract UComparisonResult execute(Object a, Object b);

    @Specialization
    static UComparisonResult doLongs(long a, long b) {
        return UComparisonResult.from(Long.compare(a, b));
    }

    @Specialization
    static UComparisonResult doBigIntegers(SBigInt a, SBigInt b) {
        return UComparisonResult.from(a.value().compareTo(b.value()));
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
    static UComparisonResult doBigDecimals(SBigDec a, SBigDec b) {
        return UComparisonResult.from(a.value().compareTo(b.value()));
    }

    @Specialization
    static UComparisonResult doFloats(float a, float b) {
        return UComparisonResult.from(Float.compare(a, b));
    }

    @Specialization
    static UComparisonResult doDoubles(double a, double b) {
        return UComparisonResult.from(Double.compare(a, b));
    }
}
