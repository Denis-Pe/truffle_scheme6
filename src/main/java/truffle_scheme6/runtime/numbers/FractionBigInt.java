package truffle_scheme6.runtime.numbers;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

import java.math.BigInteger;

@ExportLibrary(InteropLibrary.class)
public record FractionBigInt(BigInteger numerator, BigInteger denominator) {

    @ExportMessage
    String toDisplayString(boolean allowSideEffects) {
        return toString();
    }

    @Override
    public String toString() {
        return "%d/%d".formatted(numerator, denominator);
    }
}
