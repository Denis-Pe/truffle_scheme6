package truffle_scheme6.nodes.atoms.numbers;

import java.math.BigDecimal;

public final class ExactRational extends ARational {
    public final static ExactRational ZERO = new ExactRational();
    public final static ExactRational ONE = new ExactRational("1");

    public final BigDecimal value;

    public ExactRational(BigDecimal value) {
        this.value = value;
    }

    public ExactRational(String value) {
        this.value = new BigDecimal(value);
    }

    public ExactRational() {
        this.value = BigDecimal.ZERO;
    }

    @Override
    public AReal getImaginaryPart() {
        return ZERO;
    }

    @Override
    public boolean isZero() {
        return value.signum() == 0;
    }

    @Override
    public boolean isPositive() {
        return value.signum() == 1;
    }

    @Override
    public Number asInexact() {
        return new InexactReal64(value.doubleValue());
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
    public InexactReal32 asReal32() {
        return new InexactReal32(value.floatValue());
    }

    @Override
    public InexactReal64 asReal64() {
        return new InexactReal64(value.doubleValue());
    }

    @Override
    public AReal pow(InexactInteger exp) {
        return new ExactRational(value.pow((int) exp.value));
    }

    @Override
    public AReal negate() {
        return new ExactRational(value.negate());
    }
}
