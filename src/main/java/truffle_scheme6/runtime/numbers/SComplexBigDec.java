package truffle_scheme6.runtime.numbers;

import java.math.BigDecimal;

public record SComplexBigDec(BigDecimal real, BigDecimal imag) implements SComplex<BigDecimal, BigDecimal> {
}
