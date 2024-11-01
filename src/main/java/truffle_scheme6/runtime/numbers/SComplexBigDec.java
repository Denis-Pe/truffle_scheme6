package truffle_scheme6.runtime.numbers;

import java.math.BigDecimal;

public class SComplexBigDec {
    private final BigDecimal real;
    private final BigDecimal imag;

    public SComplexBigDec(BigDecimal real, BigDecimal imag) {
        this.real = real;
        this.imag = imag;
    }

    public BigDecimal getReal() {
        return real;
    }

    public BigDecimal getImag() {
        return imag;
    }
}
