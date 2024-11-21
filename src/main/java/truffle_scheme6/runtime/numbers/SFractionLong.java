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
public record SFractionLong(long numerator,
                            long denominator) implements SFraction, Comparable<SFractionLong>, TruffleObject {
    public SFractionLong(long numerator) {
        this(numerator, 1L);
    }

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

    public SFractionLong max(SFractionLong other) {
        if (compareTo(other) < 0) {
            return other;
        } else {
            return this;
        }
    }

    public SFractionLong min(SFractionLong other) {
        if (compareTo(other) > 0) {
            return other;
        } else {
            return this;
        }
    }

    public SFractionLong add(SFractionLong other) {
        return new SFractionLong(
                this.numerator * other.denominator + other.numerator * this.denominator,
                this.denominator * other.denominator
        );
    }

    public SFractionLong addExact(SFractionLong other) {
        return new SFractionLong(
                Math.addExact(Math.multiplyExact(this.numerator, other.denominator), Math.multiplyExact(other.numerator, this.denominator)),
                Math.multiplyExact(this.denominator, other.denominator)
        );
    }

    public SFractionLong subtract(SFractionLong other) {
        return new SFractionLong(
                this.numerator * other.denominator - other.numerator * this.denominator,
                this.denominator * other.denominator
        );
    }

    public SFractionLong subtractExact(SFractionLong other) {
        return new SFractionLong(
                Math.subtractExact(Math.multiplyExact(this.numerator, other.denominator), Math.multiplyExact(other.numerator, this.denominator)),
                Math.multiplyExact(this.denominator, other.denominator)
        );
    }

    public SFractionLong multiply(SFractionLong other) {
        return new SFractionLong(
                this.numerator * other.numerator,
                this.denominator * other.denominator
        );
    }

    public SFractionLong multiplyExact(SFractionLong other) {
        return new SFractionLong(
                Math.multiplyExact(this.numerator, other.numerator),
                Math.multiplyExact(this.denominator, other.denominator)
        );
    }

    public int signum() {
        return Long.signum(numerator);
    }

    public SFractionLong negate() {
        return new SFractionLong(-numerator, denominator);
    }

    public SFractionLong negateExact() {
        return new SFractionLong(Math.negateExact(numerator), denominator);
    }

    public SFractionLong abs() {
        return new SFractionLong(Math.abs(numerator), denominator);
    }

    public SFractionLong absExact() {
        return new SFractionLong(Math.absExact(numerator), denominator);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SFractionLong other) {
            return compareTo(other) == 0;
        } else if (obj instanceof SFraction other) {
            return this.asBigInt().compareTo(other.asBigInt()) == 0;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "%d/%d".formatted(numerator, denominator);
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
}
