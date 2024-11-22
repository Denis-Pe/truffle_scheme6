package truffle_scheme6.runtime.numbers;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

@ExportLibrary(InteropLibrary.class)
public record SComplexLong(SFractionLong real, SFractionLong imag) implements SComplexRational, TruffleObject {
    @Override
    public boolean isRealValued() {
        return imag.isZero();
    }

    public SComplexLong add(SComplexLong other) {
        return new SComplexLong(real.add(other.real), imag.add(other.imag));
    }

    public SComplexLong subtract(SComplexLong other) {
        return new SComplexLong(real.subtract(other.real), imag.subtract(other.imag));
    }

    public SComplexLong multiply(SComplexLong other) {
        var newReal = real.multiply(other.real).subtract(imag.multiply(other.imag));
        var newImag = real.multiply(other.imag).add(imag.multiply(other.real));
        return new SComplexLong(newReal, newImag);
    }

    public SComplexLong divide(SComplexLong other) {
        var denominator = other.real.multiply(other.real).add(other.imag.multiply(other.imag));
        var realNumerator = real.multiply(other.real).add(imag.multiply(other.imag));
        var imagNumerator = imag.multiply(other.real).subtract(real.multiply(other.imag));

        return new SComplexLong(realNumerator.divide(denominator), imagNumerator.divide(denominator));
    }

    public SComplexLong negate() {
        return new SComplexLong(real.negate(), imag.negate());
    }

    public SComplexLong inverse() {
        var denominator = real.multiply(real).add(imag.multiply(imag));
        return new SComplexLong(real.divide(denominator), imag.divide(denominator).negate());
    }

    @Override
    public String toString() {
        return real +
                (imag.signum() == -1 ? "" : "+") +
                imag + "i";
    }

    @ExportMessage
    String toDisplayString(boolean sideEffectsAllowed) {
        return toString();
    }
}
