package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.GeneratePackagePrivate;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import truffle_scheme6.nodes.STypes;
import truffle_scheme6.runtime.numbers.*;

@GeneratePackagePrivate
@TypeSystemReference(STypes.class)
abstract class UComplexComparator extends URealComparator {
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
