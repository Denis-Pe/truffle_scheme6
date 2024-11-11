package truffle_scheme6.runtime.numbers;

public record SComplexRational(SFraction real, SFraction imag) implements SComplex {
    @Override
    public boolean isRealValued() {
        return imag.isZero();
    }
}
