package truffle_scheme6.runtime.numbers;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

@ExportLibrary(InteropLibrary.class)
public record ComplexDouble(double real, double imaginary) implements TruffleObject {
    @ExportMessage
    String toDisplayString(boolean _allowSideEffects) {
        return toString();
    }

    @Override
    public String toString() {
        return real +
                (real < 0 ? "-" : "+")
                + imaginary + "i";
    }
}