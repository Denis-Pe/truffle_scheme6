package truffle_scheme6.runtime.numbers;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

import java.math.BigInteger;

@ExportLibrary(InteropLibrary.class)
public class SBigInt extends SRational implements TruffleObject {
    private final BigInteger value;

    private static final BigInteger MIN_BYTE = BigInteger.valueOf(Byte.MIN_VALUE);
    private static final BigInteger MIN_SHORT = BigInteger.valueOf(Short.MIN_VALUE);
    private static final BigInteger MIN_INT = BigInteger.valueOf(Integer.MIN_VALUE);
    private static final BigInteger MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);

    private static final BigInteger MAX_BYTE = BigInteger.valueOf(Byte.MAX_VALUE);
    private static final BigInteger MAX_SHORT = BigInteger.valueOf(Short.MAX_VALUE);
    private static final BigInteger MAX_INT = BigInteger.valueOf(Integer.MAX_VALUE);
    private static final BigInteger MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);

    public SBigInt(BigInteger value) {
        this.value = value;
    }

    public BigInteger getValue() {
        return value;
    }

    @ExportMessage
    boolean isNumber() {
        return true;
    }

    private boolean valFitsIn(BigInteger minimum, BigInteger maximum) {
        return minimum.compareTo(value) <= 0 && maximum.compareTo(value) >= 0;
    }

    @ExportMessage
    boolean fitsInByte() {
        return valFitsIn(MIN_BYTE, MAX_BYTE);
    }

    @ExportMessage
    boolean fitsInShort() {
        return valFitsIn(MIN_SHORT, MAX_SHORT);
    }

    @ExportMessage
    boolean fitsInInt() {
        return valFitsIn(MIN_INT, MAX_INT);
    }

    @ExportMessage
    boolean fitsInLong() {
        return valFitsIn(MIN_LONG, MAX_LONG);
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
        return value.floatValue();
    }

    @ExportMessage
    double asDouble() throws UnsupportedMessageException {
        return value.doubleValue();
    }

    @ExportMessage
    String toDisplayString(boolean allowSideEffects) {
        return toString();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
