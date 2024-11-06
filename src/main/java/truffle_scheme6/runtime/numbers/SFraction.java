package truffle_scheme6.runtime.numbers;

public interface SFraction {
    double doubleValue();

    default float floatValue() {
        return (float) doubleValue();
    }
    
    boolean isPerfectlyDivisible();

    default boolean isZero() {
        return equalsLong(0);
    }

    boolean equalsLong(long num);
}
