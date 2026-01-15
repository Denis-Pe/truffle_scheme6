package truffle_scheme6.runtime.numbers;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

import java.math.BigInteger;

@ExportLibrary(InteropLibrary.class)
public record SBigInt(BigInteger value) implements TruffleObject {
    public SBigInt(long value) {
        this(BigInteger.valueOf(value));
    }

    @Override
    public String toString() {
        return "#e" + value.toString();
    }

    @ExportMessage
    public boolean isNumber() {
        return true;
    }

    @ExportMessage
    boolean fitsInByte() {
        try {
            value.byteValueExact();
            return true;
        } catch (ArithmeticException e) {
            return false;
        }
    }

    @ExportMessage
    boolean fitsInShort() {
        try {
            value.shortValueExact();
            return true;
        } catch (ArithmeticException e) {
            return false;
        }
    }

    @ExportMessage
    boolean fitsInInt() {
        try {
            value.intValueExact();
            return true;
        } catch (ArithmeticException e) {
            return false;
        }
    }

    @ExportMessage
    boolean fitsInLong() {
        try {
            value.longValueExact();
            return true;
        } catch (ArithmeticException e) {
            return false;
        }
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
        try {
            return value.byteValueExact();
        } catch (ArithmeticException e) {
            throw UnsupportedMessageException.create(e);
        }
    }

    @ExportMessage
    short asShort() throws UnsupportedMessageException {
        try {
            return value.shortValueExact();
        } catch (ArithmeticException e) {
            throw UnsupportedMessageException.create(e);
        }
    }

    @ExportMessage
    int asInt() throws UnsupportedMessageException {
        try {
            return value.intValueExact();
        } catch (ArithmeticException e) {
            throw UnsupportedMessageException.create(e);
        }
    }

    @ExportMessage
    long asLong() throws UnsupportedMessageException {
        try {
            return value.longValueExact();
        } catch (ArithmeticException e) {
            throw UnsupportedMessageException.create(e);
        }
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
    String toDisplayString(boolean allowSideEffects) {
        return toString();
    }
}
