package truffle_scheme6.runtime.numbers;

public abstract class SRational extends Number {
    public boolean isZero() {
        return equalsLong(0);
    }

    public boolean isOne() {
        return equalsLong(1);
    }

    public abstract boolean equalsLong(long num);
}
