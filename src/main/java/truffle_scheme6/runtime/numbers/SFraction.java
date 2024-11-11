package truffle_scheme6.runtime.numbers;

import java.math.BigDecimal;

public interface SFraction {
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
}
