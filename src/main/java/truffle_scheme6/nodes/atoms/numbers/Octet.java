package truffle_scheme6.nodes.atoms.numbers;

public class Octet extends AInteger {
    public static final Octet ZERO = new Octet((byte) 0);
    public static final Octet ONE = new Octet((byte) 1);

    private final byte value;

    public Octet(byte value) {
        this.value = value;
    }

    @Override
    public AReal getImaginaryPart() {
        return ZERO;
    }

    @Override
    public AReal asDecimal() {
        return this.asReal64();
    }

    // todo
    //  if you insist upon negating a u8, turn into a signed int and negate it
    @Override
    public AReal negate() {
        return null;
    }

    @Override
    public boolean isPositive() {
        return true;
    }

    @Override
    public boolean isNegative() {
        return false;
    }

    @Override
    public boolean isZero() {
        return value == 0;
    }

    @Override
    public InexactReal32 asReal32() {
        return new InexactReal32(value);
    }

    @Override
    public InexactReal64 asReal64() {
        return new InexactReal64(value);
    }

    // todo
    //  more of that turning into bigger ints
    @Override
    public AReal pow(InexactInteger exp) {
        return null;
    }

    @Override
    public Number asExact() {
        return new ExactInteger(value);
    }

    @Override
    public Number asInexact() {
        return this;
    }

    @Override
    public String toString() {
        return String.valueOf(0xFF & value);
    }

    @Override
    public String dbg() {
        return "%s{%s}".formatted(this.getClass().getSimpleName(), this.toString());
    }
}
