package truffle_scheme6.runtime.numbers;

public record SComplexDouble(double real, double imag) implements SComplex {
    @Override
    public boolean isRealValued() {
        return imag == 0.0;
    }
}
