package truffle_scheme6.runtime.numbers;

import java.math.BigInteger;

public record SBigInt(BigInteger value) {
    public SBigInt(long value) {
        this(BigInteger.valueOf(value));
    }


}
