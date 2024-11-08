package truffle_scheme6.runtime.numbers;

public record SComplexFloat(float real, float imag) implements SComplex {
    @Override
    public boolean isRealValued() {
        return imag == 0.0f;
    }
}
