package truffle_scheme6.nodes.atoms.numbers;

public final class Complex extends AComplex {
    @Child
    public AReal realPart;
    @Child
    public AReal imaginaryPart;

    public Complex(AReal realPart, AReal imaginaryPart) {
        this.realPart = realPart;
        this.imaginaryPart = imaginaryPart;
    }

    @Override
    public Number asExact() {
        return new Complex((AReal) realPart.asExact(), (AReal) imaginaryPart.asExact());
    }

    @Override
    public Number asInexact() {
        return new Complex((AReal) realPart.asInexact(), (AReal) imaginaryPart.asInexact());
    }

    @Override
    public AReal getRealPart() {
        return realPart;
    }

    @Override
    public AReal getImaginaryPart() {
        return imaginaryPart;
    }

    @Override
    public String toString() {
        String sign = imaginaryPart.isPositive() ? "+" : "";

        return realPart + sign + imaginaryPart + "i";
    }
}
