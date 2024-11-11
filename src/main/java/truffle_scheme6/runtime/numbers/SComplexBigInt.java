package truffle_scheme6.runtime.numbers;

public record SComplexBigInt(SFractionBigInt real, SFractionBigInt imag) implements SComplexRational {
    @Override
    public boolean isRealValued() {
        return imag.isZero();
    }
}
