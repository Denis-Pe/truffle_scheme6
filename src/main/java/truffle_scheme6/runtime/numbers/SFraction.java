package truffle_scheme6.runtime.numbers;

import java.math.BigDecimal;

public interface SFraction {
    BigDecimal bigDecimalValue();

    /**
     *
     * @return a possibly truncated {@link SBigInt} that results from dividing this fraction's numerator by its denominator
     */
    SBigInt bigIntValue();

    /**
     *
     * @return a possibly truncated long that results from dividing this fraction's numerator by its denominator
     */
    long longValue();

    double doubleValue();

    default float floatValue() {
        return (float) doubleValue();
    }

    SFractionBigInt asBigInt();

    /**
     * In other words, is it integer-valued?
     */
    boolean isPerfectlyDivisible();

    default boolean isZero() {
        return equalsLong(0);
    }

    boolean equalsLong(long num);
}
