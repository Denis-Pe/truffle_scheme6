package truffle_scheme6.nodes.atoms.numbers;

// sign of the fraction is stored on the numerator
public final class Fraction extends ARational {
    @Child
    public AInteger numerator;
    @Child
    public AInteger denominator;

    @Override
    public AReal getImaginaryPart() {
        return InexactInteger.ZERO;
    }

    public Fraction(AInteger numerator, AInteger denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    @Override
    public InexactReal32 asReal32() {
        // todo
        return null;
    }

    @Override
    public boolean isPositive() {
        return numerator.isPositive();
    }

    @Override
    public Number asExact() {
        return new Fraction((AInteger) numerator.asExact(), (AInteger) denominator.asExact());
    }

    @Override
    public Number asInexact() {
        return new Fraction((AInteger) numerator.asInexact(), (AInteger) denominator.asInexact());
    }

    @Override
    public boolean isNegative() {
        return numerator.isNegative();
    }

    @Override
    public boolean isZero() {
        return numerator.isZero();
    }

    @Override
    public InexactReal64 asReal64() {
        // todo
        return null;
    }

    @Override
    public AReal pow(InexactInteger exp) {
        // todo
        return null;
    }

    @Override
    public AReal negate() {
        return new Fraction((AInteger) numerator.negate(), denominator);
    }

    @Override
    public String toString() {
        return numerator + "/" + denominator;
    }

    // todo simplify method that may return an integer
}
