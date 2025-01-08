package truffle_scheme6.runtime.numbers;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

@ExportLibrary(InteropLibrary.class)
public record SComplexLong(SFractionLong real, SFractionLong imag) implements SComplexRational, TruffleObject {
    public SComplexLong(long real, long imag) {
        this(new SFractionLong(real), new SFractionLong(imag));
    }
    
    @Override
    public boolean isRealValued() {
        return imag.isZero();
    }

    public SComplexLong add(SComplexLong other) {
        return new SComplexLong(real.add(other.real), imag.add(other.imag));
    }

    public SComplexLong addExact(SComplexLong other) {
        return new SComplexLong(real.addExact(other.real), imag.addExact(other.imag));
    }

    public SComplexLong subtract(SComplexLong other) {
        return new SComplexLong(real.subtract(other.real), imag.subtract(other.imag));
    }

    public SComplexLong subtractExact(SComplexLong other) {
        return new SComplexLong(real.subtractExact(other.real), imag.subtractExact(other.imag));
    }

    public SComplexLong multiply(SComplexLong other) {
        var newReal = real.multiply(other.real).subtract(imag.multiply(other.imag));
        var newImag = real.multiply(other.imag).add(imag.multiply(other.real));
        return new SComplexLong(newReal, newImag);
    }

    public SComplexLong multiplyExact(SComplexLong other) {
        var newReal = real.multiplyExact(other.real).subtractExact(imag.multiplyExact(other.imag));
        var newImag = real.multiplyExact(other.imag).addExact(imag.multiplyExact(other.real));
        return new SComplexLong(newReal, newImag);
    }

    public SComplexLong divide(SComplexLong other) {
        var denominator = other.real.multiply(other.real).add(other.imag.multiply(other.imag));
        var realNumerator = real.multiply(other.real).add(imag.multiply(other.imag));
        var imagNumerator = imag.multiply(other.real).subtract(real.multiply(other.imag));

        return new SComplexLong(realNumerator.divide(denominator), imagNumerator.divide(denominator));
    }

    public SComplexLong divideExact(SComplexLong other) {
        var denominator = other.real.multiplyExact(other.real).addExact(other.imag.multiplyExact(other.imag));
        var realNumerator = real.multiplyExact(other.real).addExact(imag.multiplyExact(other.imag));
        var imagNumerator = imag.multiplyExact(other.real).subtractExact(real.multiplyExact(other.imag));

        return new SComplexLong(realNumerator.divideExact(denominator), imagNumerator.divideExact(denominator));
    }

    public SComplexLong negate() {
        return new SComplexLong(real.negate(), imag.negate());
    }

    public SComplexLong negateExact() {
        return new SComplexLong(real.negateExact(), imag.negateExact());
    }

    public SComplexLong inverse() {
        var denominator = real.multiply(real).add(imag.multiply(imag));
        return new SComplexLong(real.divide(denominator), imag.divide(denominator).negate());
    }

    public SComplexLong inverseExact() {
        var denominator = real.multiplyExact(real).addExact(imag.multiplyExact(imag));
        return new SComplexLong(real.divideExact(denominator), imag.divideExact(denominator).negateExact());
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
