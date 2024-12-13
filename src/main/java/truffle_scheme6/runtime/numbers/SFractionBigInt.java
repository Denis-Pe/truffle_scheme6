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
public record SFractionBigInt(BigInteger numerator,
                              BigInteger denominator) implements SFraction, Comparable<SFractionBigInt>, TruffleObject {
    public SFractionBigInt(BigInteger numerator, BigInteger denominator) {
        this.numerator = numerator;

        if (denominator.compareTo(BigInteger.ONE) < 0) {
            throw new IllegalArgumentException("denominator must be greater than zero");
        } else {
            this.denominator = this.numerator.equals(BigInteger.ZERO) ? BigInteger.ONE : denominator;
        }
    }

    public SFractionBigInt(BigInteger numerator) {
        this(numerator, BigInteger.ONE);
    }

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
    public SFractionBigInt asBigInt() {
        return this;
    }

    @Override
    public int compareTo(SFractionBigInt other) {
        return (numerator.multiply(other.denominator))
                .compareTo(other.numerator.multiply(denominator));
    }

    public SFractionBigInt max(SFractionBigInt other) {
        if (compareTo(other) < 0) {
            return other;
        } else {
            return this;
        }
    }

    public SFractionBigInt min(SFractionBigInt other) {
        if (compareTo(other) > 0) {
            return other;
        } else {
            return this;
        }
    }

    public SFractionBigInt add(SFractionBigInt other) {
        return new SFractionBigInt(
                this.numerator.multiply(other.denominator).add(other.numerator.multiply(this.denominator)),
                this.denominator.multiply(other.denominator)
        );
    }

    public SFractionBigInt subtract(SFractionBigInt other) {
        return new SFractionBigInt(
                this.numerator.multiply(other.denominator).subtract(other.numerator.multiply(this.denominator)),
                this.denominator.multiply(other.denominator)
        );
    }

    public SFractionBigInt multiply(SFractionBigInt other) {
        return new SFractionBigInt(
                this.numerator.multiply(other.numerator),
                this.denominator.multiply(other.denominator)
        );
    }

    public SFractionBigInt divide(SFractionBigInt other) {
        BigInteger signum = BigInteger.valueOf((long) this.signum() * other.signum());
        return new SFractionBigInt(
                signum.multiply(numerator.multiply(other.denominator).abs()),
                denominator.multiply(other.numerator).abs()
        );
    }

    public int signum() {
        return numerator.signum();
    }

    public SFractionBigInt negate() {
        return new SFractionBigInt(numerator.negate(), denominator);
    }

    public SFractionBigInt abs() {
        return new SFractionBigInt(numerator.abs(), denominator);
    }

    public SFractionBigInt inverse() {
        return new SFractionBigInt(denominator.multiply(BigInteger.valueOf(numerator.signum())), numerator.abs());
    }

    /**
     * Returns the highest <b>positive</b> greatest common divisor
     */
    public BigInteger gcd() {
        return numerator.gcd(denominator);
    }

    public SFractionBigInt simplified() {
        var gcd = gcd();
        return new SFractionBigInt(numerator.divide(gcd), denominator.divide(gcd));
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
}
