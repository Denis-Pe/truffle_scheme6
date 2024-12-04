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
    public SFractionLong(long numerator, long denominator) {
        this.numerator = numerator;
        this.denominator = denominator;

        if (denominator < 1)
            throw new IllegalArgumentException("denominator must be greater than zero");
    }

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

    public SFractionLong divide(SFractionLong other) {
        long signum = (long) this.signum() * other.signum();
        return new SFractionLong(
                signum * Math.abs(numerator * other.denominator),
                Math.abs(denominator * other.numerator)
        );
    }

    public SFractionLong divideExact(SFractionLong other) {
        long signum = (long) this.signum() * other.signum();
        return new SFractionLong(
                Math.multiplyExact(signum, Math.absExact(Math.multiplyExact(numerator, other.denominator))),
                Math.absExact(Math.multiplyExact(denominator, other.numerator))
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

    public SFractionLong inverse() {
        return new SFractionLong(signum() * denominator, Math.abs(numerator));
    }

    public SFractionLong inverseExact() {
        return new SFractionLong(Math.multiplyExact(signum(), denominator), Math.absExact(numerator));
    }

    /**
     * Returns the highest <b>positive</b> greatest common divisor
     */
    public long gcd() {
        long a = Math.abs(numerator);
        long b = denominator;

        long d = 0;
        while (a % 2 == 0 && b % 2 == 0) {
            a /= 2;
            b /= 2;
            d++;
        }

        while (a % 2 == 0) a /= 2;
        while (b % 2 == 0) b /= 2;

        while (a != b) {
            long max = Math.max(a, b);
            b = Math.min(a, b);
            a = max;

            a = a - b;

            while (a % 2 == 0) a /= 2;
        }

        return (2L << d) * a;
    }

    /**
     * Like gcd(), but using exact math operations
     */
    public long gcdExact() {
        long a = Math.absExact(numerator);
        long b = denominator;

        long d = 0;
        while (a % 2 == 0 && b % 2 == 0) {
            a = Math.divideExact(a, 2L);
            b = Math.divideExact(b, 2L);
            d = Math.incrementExact(d);
        }

        while (a % 2 == 0) a = Math.divideExact(a, 2L);
        while (b % 2 == 0) b = Math.divideExact(b, 2L);

        while (a != b) {
            long max = Math.max(a, b);
            b = Math.min(a, b);
            a = max;

            a = Math.subtractExact(a, b);

            while (a % 2 == 0) a = Math.divideExact(a, 2L);
        }
        
        if (d >= 63L) throw new ArithmeticException();
        return Math.multiplyExact(2L << d, a);
    }

    public SFractionLong simplified() {
        long gcd = gcd();
        return new SFractionLong(numerator / gcd, denominator / gcd);
    }

    public SFractionLong simplifiedExact() {
        long gcd = gcdExact();
        return new SFractionLong(Math.divideExact(numerator, gcd), Math.divideExact(denominator, gcd));
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
