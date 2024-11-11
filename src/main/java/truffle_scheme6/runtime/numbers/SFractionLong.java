package truffle_scheme6.runtime.numbers;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

@ExportLibrary(InteropLibrary.class)
public record SFractionLong(long numerator, long denominator) implements SFraction, Comparable<SFractionLong>, TruffleObject {

    @Override
    public boolean equalsLong(long num) {
        return numerator / denominator == num && numerator % denominator == 0;
    }

    @Override
    public boolean isPerfectlyDivisible() {
        return numerator % denominator == 0;
    }

    @Override
    public float floatValue() {
        return ((float) numerator) / ((float) denominator);
    }

    @Override
    public double doubleValue() {
        return ((double) numerator) / ((double) denominator);
    }

    @Override
    public BigDecimal bigDecimalValue() {
        return BigDecimal.valueOf(numerator).divide(BigDecimal.valueOf(denominator), MathContext.DECIMAL128);
    }

    @Override
    public SFractionBigInt asBigInt() {
        return new SFractionBigInt(
                BigInteger.valueOf(numerator),
                BigInteger.valueOf(denominator)
        );
    }

    @Override
    public int compareTo(SFractionLong other) {
        return Long.compare(numerator * other.denominator, other.numerator * denominator);
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
    boolean fitsInDouble() {
        return true;
    }

    @ExportMessage
    boolean fitsInFloat() {
        return true;
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
    boolean fitsInShort() {
        return false;
    }

    @ExportMessage
    byte asByte() throws UnsupportedMessageException {
        throw UnsupportedMessageException.create();
    }

    @ExportMessage
    double asDouble() {
        return doubleValue();
    }

    @ExportMessage
    float asFloat() {
        return floatValue();
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
    short asShort() throws UnsupportedMessageException {
        throw UnsupportedMessageException.create();
    }

    @ExportMessage
    String toDisplayString(boolean allowSideEffects) {
        return toString();
    }

    @Override
    public String toString() {
        return "%d/%d".formatted(numerator, denominator);
    }
}
