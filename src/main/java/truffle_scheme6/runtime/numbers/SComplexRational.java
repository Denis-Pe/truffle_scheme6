package truffle_scheme6.runtime.numbers;

public class SComplexRational {
    private final SRational real;
    private final SRational imag;

    public SComplexRational(SRational real, SRational imag) {
        this.real = real;
        this.imag = imag;
    }

    public SRational getReal() {
        return real;
    }

    public SRational getImag() {
        return imag;
    }
}
