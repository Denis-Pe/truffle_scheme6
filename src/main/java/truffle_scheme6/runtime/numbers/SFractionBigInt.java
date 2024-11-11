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
public record SFractionBigInt(BigInteger numerator, BigInteger denominator) implements SFraction, Comparable<SFractionBigInt>, TruffleObject {

    @Override
    public boolean equalsLong(long num) {
        var divRem = numerator.divideAndRemainder(denominator);
        var div = divRem[0];
        var rem = divRem[1];

        return div.equals(BigInteger.valueOf(num)) && rem.equals(BigInteger.ZERO);
    }

    @Override
    public boolean isPerfectlyDivisible() {
        return numerator.remainder(denominator).equals(BigInteger.ZERO);
    }

    @Override
    public float floatValue() {
        return numerator.floatValue() / denominator.floatValue();
    }

    @Override
    public double doubleValue() {
        return numerator.doubleValue() / denominator.doubleValue();
    }

    @Override
    public BigDecimal bigDecimalValue() {
        return new BigDecimal(numerator).divide(new BigDecimal(denominator), MathContext.DECIMAL128);
    }

    @Override
    public int compareTo(SFractionBigInt other) {
        return (numerator.multiply(other.denominator))
                .compareTo(other.numerator.multiply(denominator));
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
        return this.doubleValue();
    }

    @ExportMessage
    float asFloat() {
        return this.floatValue();
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
