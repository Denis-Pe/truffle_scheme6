package truffle_scheme6.runtime.numbers;


import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

@ExportLibrary(InteropLibrary.class)
public record SComplexBigInt(SFractionBigInt real, SFractionBigInt imag) implements SComplexRational, TruffleObject {
    @Override
    public boolean isRealValued() {
        return imag.isZero();
    }

    @Override
    public String toString() {
        return real.toString() +
                (imag.signum() == -1 ? "" : "+") +
                imag + "i";
    }

    @ExportMessage
    String toDisplayString(boolean sideEffectsAllowed) {
        return toString();
    }
}
