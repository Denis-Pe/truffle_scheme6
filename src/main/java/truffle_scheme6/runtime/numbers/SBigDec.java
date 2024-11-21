package truffle_scheme6.runtime.numbers;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

import java.math.BigDecimal;
import java.math.BigInteger;

@ExportLibrary(InteropLibrary.class)
public record SBigDec(BigDecimal value) implements TruffleObject {
    public SBigDec(double value) {
        this(BigDecimal.valueOf(value));
    }

    public SBigDec(long value) {
        this(BigDecimal.valueOf(value));
    }

    public SBigDec(BigInteger value) {
        this(new BigDecimal(value));
    }

    public SBigDec(SBigInt value) {
        this(value.value());
    }

    @ExportMessage
    boolean isNumber() {
        return true;
    }

    @ExportMessage
    boolean fitsInByte() {
        return false;
    }

    @ExportMessage
    boolean fitsInShort() {
        return false;
    }

    @ExportMessage
    boolean fitsInInt() {
        return false;
    }

    @ExportMessage
    boolean fitsInLong() {
        return false;
    }

    @ExportMessage
    boolean fitsInFloat() {
        return !Float.isInfinite(value.floatValue());
    }

    @ExportMessage
    boolean fitsInDouble() {
        return !Double.isInfinite(value.doubleValue());
    }

    @ExportMessage
    byte asByte() throws UnsupportedMessageException {
        throw UnsupportedMessageException.create();
    }

    @ExportMessage
    short asShort() throws UnsupportedMessageException {
        throw UnsupportedMessageException.create();
    }

    @ExportMessage
    int asInt() throws UnsupportedMessageException {
        throw UnsupportedMessageException.create();
    }

    @ExportMessage
    long asLong() throws UnsupportedMessageException {
        throw UnsupportedMessageException.create();
    }

    @ExportMessage
    float asFloat() throws UnsupportedMessageException {
        float floatValue = value.floatValue();

        if (Float.isInfinite(floatValue)) {
            throw UnsupportedMessageException.create();
        } else {
            return floatValue;
        }
    }

    @ExportMessage
    double asDouble() throws UnsupportedMessageException {
        double doubleValue = value.doubleValue();

        if (Double.isInfinite(doubleValue)) {
            throw UnsupportedMessageException.create();
        } else {
            return doubleValue;
        }
    }

    @ExportMessage
    String toDisplayString(boolean sideEffectsAllowed) {
        return toString();
    }
}
