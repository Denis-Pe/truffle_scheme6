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

    public SComplexBigInt add(SComplexBigInt other) {
        return new SComplexBigInt(real.add(other.real), imag.add(other.imag));
    }
    
    public SComplexBigInt subtract(SComplexBigInt other) {
        return new SComplexBigInt(real.subtract(other.real), imag.subtract(other.imag));
    }

    public SComplexBigInt multiply(SComplexBigInt other) {
        var newReal = real.multiply(other.real).subtract(imag.multiply(other.imag));
        var newImag = real.multiply(other.imag).add(imag.multiply(other.real));
        return new SComplexBigInt(newReal, newImag);
    }
    
    public SComplexBigInt negate() {
        return new SComplexBigInt(real.negate(), imag.negate());
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
