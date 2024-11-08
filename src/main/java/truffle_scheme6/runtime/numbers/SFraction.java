package truffle_scheme6.runtime.numbers;

import truffle_scheme6.builtins.private_utils.NumericallyComparable;

public interface SFraction extends NumericallyComparable {
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
