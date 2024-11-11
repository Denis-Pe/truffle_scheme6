package truffle_scheme6.runtime.numbers;

public record SComplexLong(SFractionLong real, SFractionLong imag) implements SComplexRational {
    @Override
    public boolean isRealValued() {
        return imag.isZero();
    }
}
