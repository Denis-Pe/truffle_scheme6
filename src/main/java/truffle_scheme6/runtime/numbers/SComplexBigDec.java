package truffle_scheme6.runtime.numbers;

import java.math.BigDecimal;

public class SComplexBigDec implements SComplex<BigDecimal, BigDecimal> {
    private final BigDecimal real;
    private final BigDecimal imag;

    public SComplexBigDec(BigDecimal real, BigDecimal imag) {
        this.real = real;
        this.imag = imag;
    }

    @Override
    public BigDecimal getReal() {
        return real;
    }

    @Override
    public BigDecimal getImag() {
        return imag;
    }
}
