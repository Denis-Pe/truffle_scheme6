package p.denis.atoms.numbers;

import java.math.BigInteger;

public final class ExactInteger extends AInteger {
    public final static ExactInteger ZERO = new ExactInteger();
    public final static ExactInteger ONE = new ExactInteger(1);

    public final BigInteger value;

    public ExactInteger() {
        this.value = BigInteger.ZERO;
    }

    public ExactInteger(BigInteger value) {
        this.value = value;
    }

    public ExactInteger(long value) {
        this.value = new BigInteger(String.valueOf(value));
    }

    public ExactInteger(String value) {
        this.value = new BigInteger(value);
    }

    public ExactInteger(String value, int radix) {
        this.value = new BigInteger(value, radix);
    }

    @Override
    public AReal getImaginaryPart() {
        return ZERO;
    }

    @Override
    public AReal asDecimal() {
        return new ExactRational(value.toString());
    }

    @Override
    public InexactReal32 asReal32() {
        return new InexactReal32(value.floatValue());
    }

    @Override
    public InexactReal64 asReal64() {
        return new InexactReal64(value.doubleValue());
    }

    @Override
    public AReal pow(InexactInteger exp) {
        return new ExactInteger(value.pow((int) exp.value));
    }

    @Override
    public boolean isPositive() {
        return value.signum() == 1;
    }

    @Override
    public boolean isZero() {
        return value.signum() == 0;
    }

    @Override
    public Number asInexact() {
        return new InexactInteger(value.longValue());
    }

    @Override
    public Number asExact() {
        return this;
    }

    @Override
    public boolean isNegative() {
        return value.signum() == -1;
    }

    @Override
    public AReal negate() {
        return new ExactInteger(value.negate());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
