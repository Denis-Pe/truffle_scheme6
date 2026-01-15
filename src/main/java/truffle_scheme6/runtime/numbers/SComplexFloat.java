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

    public SComplexFloat add(SComplexFloat other) {
        return new SComplexFloat(real + other.real, imag + other.imag);
    }

    public SComplexFloat subtract(SComplexFloat other) {
        return new SComplexFloat(real - other.real, imag - other.imag);
    }

    public SComplexFloat multiply(SComplexFloat other) {
        var newReal = real * other.real - imag * other.imag;
        var newImag = real * other.imag + imag * other.real;
        return new SComplexFloat(newReal, newImag);
    }

    public SComplexFloat divide(SComplexFloat other) {
        var denominator = other.real * other.real + other.imag * other.imag;
        var realNumerator = real * other.real + imag * other.imag;
        var imagNumerator = imag * other.real - real * other.imag;

        return new SComplexFloat(realNumerator / denominator, imagNumerator / denominator);
    }

    public SComplexFloat negate() {
        return new SComplexFloat(-real, -imag);
    }

    public SComplexFloat inverse() {
        var denominator = real * real + imag * imag;
        return new SComplexFloat(real / denominator, -(imag / denominator));
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
