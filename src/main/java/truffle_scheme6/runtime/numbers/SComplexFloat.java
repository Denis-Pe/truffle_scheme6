package truffle_scheme6.runtime.numbers;

public class SComplexFloat {
    private final float real;
    private final float imag;

    public SComplexFloat(float real, float imag) {
        this.real = real;
        this.imag = imag;
    }

    public float getReal() {
        return real;
    }

    public float getImag() {
        return imag;
    }
}
