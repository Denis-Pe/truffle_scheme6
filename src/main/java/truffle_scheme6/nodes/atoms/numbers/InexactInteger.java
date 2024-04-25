package truffle_scheme6.nodes.atoms.numbers;

public final class InexactInteger extends AInteger {
    public static final InexactInteger ZERO = new InexactInteger(0);
    public static final InexactInteger ONE = new InexactInteger(1);

    public final long value;

    public InexactInteger() {
        this.value = 0;
    }

    public InexactInteger(long value) {
        this.value = value;
    }

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
    public Number asExact() {
        return new ExactInteger(value);
    }

    public InexactInteger(String value) {
        this.value = Long.parseLong(value);
    }

    public InexactInteger(String value, int radix) {
        this.value = Long.parseLong(value, radix);
    }

    @Override
    public AReal negate() {
        return new InexactInteger(-value);
    }

    @Override
    public AReal asDecimal() {
        return new InexactReal64(value);
    }

    @Override
    public InexactReal32 asReal32() {
        return new InexactReal32((float) value);
    }

    @Override
    public InexactReal64 asReal64() {
        return new InexactReal64((double) value);
    }

    @Override
    public AReal getImaginaryPart() {
        return ZERO;
    }

    @Override
    public boolean isZero() {
        return value == 0L;
    }

    @Override
    public AReal pow(InexactInteger exp) {
        // todo
        //  what if exp is negative?
        if (exp.value == 0) {
            return new InexactInteger(0);
        } else {
            var newV = value;

            for (int i = 0; i < exp.value; i++) {
                newV *= value;
            }

            return new InexactInteger(newV);
        }
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
