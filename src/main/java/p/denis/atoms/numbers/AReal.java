package p.denis.atoms.numbers;

public abstract class AReal extends AComplex {
    // a requirement that is not expressible in Java is that
    // all subclasses need a string constructor

    // returns the same thing but in negative
    // intended to return a new one, not modify itself
    public abstract AReal negate();

    public abstract boolean isPositive();

    public abstract boolean isNegative();

    public abstract boolean isZero();

    public abstract InexactReal32 asReal32();

    public abstract InexactReal64 asReal64();

    public abstract AReal pow(InexactInteger exp);

    @Override
    public AReal getRealPart() {
        return this;
    }
}
