package truffle_scheme6.runtime.numbers;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

@ExportLibrary(InteropLibrary.class)
public record SComplexFloat(float real, float imag) implements SComplex, TruffleObject {
    @Override
    public boolean isRealValued() {
        return imag == 0.0f;
    }

    @Override
    public String toString() {
        return real +
                (Math.signum(imag) == -1.0f ? "" : "+") +
                imag + "i";
    }

    @ExportMessage
    String toDisplayString(boolean sideEffectsAllowed) {
        return toString();
    }
}
