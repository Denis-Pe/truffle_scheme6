package truffle_scheme6.runtime.numbers;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

@ExportLibrary(InteropLibrary.class)
public record ComplexLong(long real, long imaginary) {
    @ExportMessage
    String toDisplayString(boolean _allowSideEffects) {
        return toString();
    }

    @Override
    public String toString() {
        return real +
                (imaginary < 0 ? "-" : "+")
                + imaginary + "i";
    }
}