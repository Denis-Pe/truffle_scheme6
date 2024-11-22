package truffle_scheme6.runtime.numbers;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

import java.math.BigDecimal;
import java.math.MathContext;

@ExportLibrary(InteropLibrary.class)
public record SComplexBigDec(BigDecimal real, BigDecimal imag) implements SComplex, TruffleObject {
    @Override
    public boolean isRealValued() {
        return imag.compareTo(BigDecimal.ZERO) == 0;
    }

    public SComplexBigDec add(SComplexBigDec other) {
        return new SComplexBigDec(real.add(other.real), imag.add(other.imag));
    }

    public SComplexBigDec subtract(SComplexBigDec other) {
        return new SComplexBigDec(real.subtract(other.real), imag.subtract(other.imag));
    }

    public SComplexBigDec multiply(SComplexBigDec other) {
        var newReal = real.multiply(other.real).subtract(imag.multiply(other.imag));
        var newImag = real.multiply(other.imag).add(imag.multiply(other.real));
        return new SComplexBigDec(newReal, newImag);
    }

    public SComplexBigDec divide(SComplexBigDec other) {
        var denominator = other.real.multiply(other.real).add(other.imag.multiply(other.imag));
        var realNumerator = real.multiply(other.real).add(imag.multiply(other.imag));
        var imagNumerator = imag.multiply(other.real).subtract(real.multiply(other.imag));

        return new SComplexBigDec(realNumerator.divide(denominator, MathContext.DECIMAL128), imagNumerator.divide(denominator, MathContext.DECIMAL128));
    }

    public SComplexBigDec negate() {
        return new SComplexBigDec(real.negate(), imag.negate());
    }

    public SComplexBigDec inverse() {
        var denominator = real.multiply(real).add(imag.multiply(imag));
        return new SComplexBigDec(real.divide(denominator, MathContext.DECIMAL128), imag.divide(denominator, MathContext.DECIMAL128).negate());
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
