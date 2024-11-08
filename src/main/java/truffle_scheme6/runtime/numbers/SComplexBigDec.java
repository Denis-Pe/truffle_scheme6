package truffle_scheme6.runtime.numbers;

import java.math.BigDecimal;

public record SComplexBigDec(BigDecimal real, BigDecimal imag) implements SComplex {
    @Override
    public boolean isRealValued() {
        return imag.compareTo(BigDecimal.ZERO) == 0;
    }
}
