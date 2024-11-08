package truffle_scheme6.runtime.numbers;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import truffle_scheme6.builtins.private_utils.ComparisonResult;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

@ExportLibrary(InteropLibrary.class)
public record SFractionLong(long numerator, long denominator) implements SFraction, TruffleObject {

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

    public SFractionBigInt asBigInt() {
        return new SFractionBigInt(
                BigInteger.valueOf(numerator),
                BigInteger.valueOf(denominator)
        );
    }

    @Override
    public ComparisonResult compareTo(long n) {
        var comp = Long.compare(numerator / denominator, n);

        if (comp == 0) {
            return numerator % denominator == 0 ? ComparisonResult.Equal : ComparisonResult.GreaterThan;
        } else {
            return ComparisonResult.from(comp);
        }
    }

    @Override
    public ComparisonResult compareTo(BigInteger n) {
        return this.asBigInt().compareTo(n);
    }

    @Override
    public ComparisonResult compareTo(SFractionBigInt q) {
        return this.asBigInt().compareTo(q);
    }

    @Override
    public ComparisonResult compareTo(SFractionLong other) {
        var div1 = numerator / denominator;
        var rem1 = numerator % denominator;
        var div2 = other.numerator / other.denominator;
        var rem2 = other.numerator % other.denominator;

        var divComp = Long.compare(div1, div2);
        if (divComp == 0) {
            return ComparisonResult.from(Long.compare(rem1, rem2));
        } else {
            return ComparisonResult.from(divComp);
        }
    }

    @Override
    public ComparisonResult compareTo(BigDecimal x) {
        return ComparisonResult.from((BigDecimal.valueOf(numerator).divide(BigDecimal.valueOf(denominator), MathContext.DECIMAL128)).compareTo(x));
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
