package truffle_scheme6.nodes.atoms.numbers;

import java.math.BigDecimal;

public final class InexactReal32 extends ARational {
    public static final InexactReal32 NEG_INF = new InexactReal32(Float.NEGATIVE_INFINITY);
    public static final InexactReal32 POS_INF = new InexactReal32(Float.POSITIVE_INFINITY);
    public static final InexactReal32 NAN = new InexactReal32(Float.NaN);
    public static final InexactReal32 ZERO = new InexactReal32(0);
    public static final InexactReal32 ONE = new InexactReal32(1);

    public final float value;

    public InexactReal32(float value) {
        this.value = value;
    }

    public InexactReal32(String value) {
        this.value = Float.parseFloat(value);
    }

    @Override
    public InexactReal32 asReal32() {
        return this;
    }

    @Override
    public boolean isPositive() {
        return value > 0;
    }

    @Override
    public Number asExact() {
        return new ExactRational(new BigDecimal(value));
    }

    @Override
    public Number asInexact() {
        return this;
    }

    @Override
    public boolean isNegative() {
        return value < 0;
    }

    @Override
    public boolean isZero() {
        return value == 0;
    }

    @Override
    public AReal pow(InexactInteger exp) {
        return new InexactReal32((float) Math.pow(value, exp.value));
    }

    @Override
    public AReal getImaginaryPart() {
        return ZERO;
    }

    @Override
    public InexactReal64 asReal64() {
        return new InexactReal64(value);
    }

    @Override
    public AReal negate() {
        return new InexactReal32(-value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
