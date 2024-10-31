package truffle_scheme6.runtime.numbers;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

import java.math.BigInteger;

@ExportLibrary(InteropLibrary.class)
public record ComplexBigInt(BigInteger real, BigInteger imaginary) {
    @ExportMessage
    String toDisplayString(boolean _allowSideEffects) {
        return toString();
    }

    @Override
    public String toString() {
        return real +
                (imaginary.signum() == -1 ? "-" : "+") +
                imaginary + "i";
    }
}