package truffle_scheme6.runtime.numbers;

public class SComplexDouble implements SNumber {
    private final double real;
    private final double imag;

    public SComplexDouble(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }

    public double getReal() {
        return real;
    }

    public double getImag() {
        return imag;
    }
}
