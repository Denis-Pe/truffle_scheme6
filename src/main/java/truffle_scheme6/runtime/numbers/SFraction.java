package truffle_scheme6.runtime.numbers;

import truffle_scheme6.builtins.private_utils.ComparisonResult;
import truffle_scheme6.builtins.private_utils.NumericallyComparable;

import java.math.BigDecimal;

public interface SFraction extends NumericallyComparable {
    BigDecimal bigDecimalValue();

    double doubleValue();

    default float floatValue() {
        return (float) doubleValue();
    }

    /**
     * In other words, is it integer-valued?
     */
    boolean isPerfectlyDivisible();

    default boolean isZero() {
        return equalsLong(0);
    }

    boolean equalsLong(long num);

    @Override
    default ComparisonResult compareTo(float x) {
        return ComparisonResult.from(
                Float.compare(this.floatValue(), x)
        );
    }

    @Override
    default ComparisonResult compareTo(double x) {
        return ComparisonResult.from(
                Double.compare(this.doubleValue(), x)
        );
    }

    @Override
    default ComparisonResult compareTo(BigDecimal x) {
        return ComparisonResult.from(this.bigDecimalValue().compareTo(x));
    }

    @Override
    default ComparisonResult compareTo(SComplexBigDec z) {
        return z.isRealValued() ? compareTo(z.real()) : ComparisonResult.Unequal;
    }

    @Override
    default ComparisonResult compareTo(SComplexDouble z) {
        return z.isRealValued() ? compareTo(z.real()) : ComparisonResult.Unequal;
    }

    @Override
    default ComparisonResult compareTo(SComplexFloat z) {
        return z.isRealValued() ? compareTo(z.real()) : ComparisonResult.Unequal;
    }

    @Override
    default ComparisonResult compareTo(SComplexRational z) {
        if (z.isRealValued()) {
            return switch (z.real()) {
                case SFractionBigInt fb -> compareTo(fb);
                case SFractionLong fl -> compareTo(fl);
                default -> throw new IllegalStateException("Unexpected value: " + z.real());
            };
        } else {
            return ComparisonResult.Unequal;
        }
    }
}
