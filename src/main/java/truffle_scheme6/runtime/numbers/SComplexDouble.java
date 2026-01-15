package truffle_scheme6.runtime.numbers;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

@ExportLibrary(InteropLibrary.class)
public record SComplexDouble(double real, double imag) implements SComplex, TruffleObject {
    @Override
    public boolean isRealValued() {
        return imag == 0.0;
    }

    public SComplexDouble add(SComplexDouble other) {
        return new SComplexDouble(real + other.real, imag + other.imag);
    }
    
    public SComplexDouble subtract(SComplexDouble other) {
        return new SComplexDouble(real - other.real, imag - other.imag);
    }

    public SComplexDouble multiply(SComplexDouble other) {
        var newReal = real * other.real - imag * other.imag;
        var newImag = real * other.imag + imag * other.real;
        return new SComplexDouble(newReal, newImag);
    }

    public SComplexDouble divide(SComplexDouble other) {
        var denominator = other.real * other.real + other.imag * other.imag;
        var realNumerator = real * other.real + imag * other.imag;
        var imagNumerator = imag * other.real - real * other.imag;

        return new SComplexDouble(realNumerator / denominator, imagNumerator / denominator);
    }
    
    public SComplexDouble negate() {
        return new SComplexDouble(-real, -imag);
    }

    public SComplexDouble inverse() {
        var denominator = real * real + imag * imag;
        return new SComplexDouble(real / denominator, -(imag / denominator));
    }

    @Override
    public String toString() {
        return real +
                (Math.signum(imag) == -1.0 ? "" : "+") +
                imag + "i";
    }

    @ExportMessage
    String toDisplayString(boolean sideEffectsAllowed) {
        return toString();
    }
}
