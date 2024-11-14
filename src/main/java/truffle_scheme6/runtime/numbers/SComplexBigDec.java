package truffle_scheme6.runtime.numbers;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

import java.math.BigDecimal;

@ExportLibrary(InteropLibrary.class)
public record SComplexBigDec(BigDecimal real, BigDecimal imag) implements SComplex, TruffleObject {
    @Override
    public boolean isRealValued() {
        return imag.compareTo(BigDecimal.ZERO) == 0;
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
