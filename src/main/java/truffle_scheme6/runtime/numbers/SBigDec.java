package truffle_scheme6.runtime.numbers;

import java.math.BigDecimal;
import java.math.BigInteger;

public record SBigDec(BigDecimal value) {
    public SBigDec(double value) {
        this(BigDecimal.valueOf(value));
    }

    public SBigDec(long value) {
        this(BigDecimal.valueOf(value));
    }

    public SBigDec(BigInteger value) {
        this(new BigDecimal(value));
    }

    public SBigDec(SBigInt value) {
        this(value.value());
    }
}
