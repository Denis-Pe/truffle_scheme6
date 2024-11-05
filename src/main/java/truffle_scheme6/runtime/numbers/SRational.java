package truffle_scheme6.runtime.numbers;

public interface SRational extends SReal {
    default boolean isZero() {
        return equalsLong(0);
    }

    default boolean isOne() {
        return equalsLong(1);
    }

    boolean equalsLong(long num);
}
