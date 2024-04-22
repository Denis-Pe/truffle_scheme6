package p.denis.atoms.numbers;

import java.math.BigDecimal;

public final class InexactReal64 extends ARational {
    public static final InexactReal64 POS_INF = new InexactReal64(Double.POSITIVE_INFINITY);
    public static final InexactReal64 NEG_INF = new InexactReal64(Double.NEGATIVE_INFINITY);
    public static final InexactReal64 NAN = new InexactReal64(Double.NaN);
    public static final InexactReal64 ZERO = new InexactReal64(0);
    public static final InexactReal64 ONE = new InexactReal64(1);

    public final double value;

    @Override
    public boolean isPositive() {
        return value > 0;
    }

    @Override
    public boolean isNegative() {
        return value < 0;
    }

    @Override
    public Number asInexact() {
        return this;
    }

    @Override
    public boolean isZero() {
        return value == 0;
    }

    @Override
    public AReal getImaginaryPart() {
        return ZERO;
    }

    @Override
    public Number asExact() {
        return new ExactRational(new BigDecimal(value));
    }

    public InexactReal64(double value) {
        this.value = value;
    }

    public InexactReal64(String value) {
        this.value = Double.parseDouble(value);
    }

    @Override
    public InexactReal32 asReal32() {
        return new InexactReal32((float) value);
    }

    @Override
    public InexactReal64 asReal64() {
        return this;
    }

    @Override
    public AReal pow(InexactInteger exp) {
        return new InexactReal64(Math.pow(value, exp.value));
    }

    @Override
    public AReal negate() {
        return new InexactReal64(-value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
