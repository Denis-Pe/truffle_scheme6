package truffle_scheme6.runtime.numbers;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

@ExportLibrary(InteropLibrary.class)
public class SFixnum extends SRational implements TruffleObject {
    private final long value;

    private static final long MIN_BYTE = Byte.MIN_VALUE;
    private static final long MIN_SHORT = Short.MIN_VALUE;
    private static final long MIN_INT = Integer.MIN_VALUE;

    private static final long MAX_BYTE = Byte.MAX_VALUE;
    private static final long MAX_SHORT = Short.MAX_VALUE;
    private static final long MAX_INT = Integer.MAX_VALUE;

    public SFixnum(long value) {
        this.value = value;
    }

    @ExportMessage
    boolean isNumber() {
        return true;
    }

    @ExportMessage
    boolean fitsInByte() {
        return MIN_BYTE <= value && value <= MAX_BYTE;
    }

    @ExportMessage
    boolean fitsInShort() {
        return MIN_SHORT <= value && value <= MAX_SHORT;
    }

    @ExportMessage
    boolean fitsInInt() {
        return MIN_INT <= value && value <= MAX_INT;
    }

    @ExportMessage
    boolean fitsInLong() {
        return true;
    }

    @ExportMessage
    boolean fitsInFloat() {
        return true;
    }

    @ExportMessage
    boolean fitsInDouble() {
        return true;
    }

    @ExportMessage
    byte asByte() throws UnsupportedMessageException {
        if (fitsInByte()) {
            return (byte) value;
        } else {
            throw UnsupportedMessageException.create();
        }
    }

    @ExportMessage
    short asShort() throws UnsupportedMessageException {
        if (fitsInShort()) {
            return (short) value;
        } else {
            throw UnsupportedMessageException.create();
        }
    }

    @ExportMessage
    int asInt() throws UnsupportedMessageException {
        if (fitsInInt()) {
            return (int) value;
        } else {
            throw UnsupportedMessageException.create();
        }
    }

    @ExportMessage
    long asLong() throws UnsupportedMessageException {
        return value;
    }

    @ExportMessage
    float asFloat() throws UnsupportedMessageException {
        return (float) value;
    }

    @ExportMessage
    double asDouble() throws UnsupportedMessageException {
        return (double) value;
    }

    @ExportMessage
    String toDisplayString(boolean allowSideEffects) {
        return toString();
    }

    @Override
    public String toString() {
        return Long.toString(value);
    }
}
