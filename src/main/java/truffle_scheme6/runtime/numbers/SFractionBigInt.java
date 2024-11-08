package truffle_scheme6.runtime.numbers;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import truffle_scheme6.builtins.private_utils.ComparisonResult;
import truffle_scheme6.builtins.private_utils.NumericallyComparable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

@ExportLibrary(InteropLibrary.class)
public record SFractionBigInt(BigInteger numerator, BigInteger denominator) implements SFraction, TruffleObject {

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
    public ComparisonResult compareTo(long n) {
        return compareTo(BigInteger.valueOf(n));
    }

    @Override
    public ComparisonResult compareTo(BigInteger n) {
        var divRem = numerator.divideAndRemainder(denominator);
        var div = divRem[0];
        var rem = divRem[1];

        var comparison = div.compareTo(n);

        if (comparison == 0) {
            return rem.equals(BigInteger.ZERO) ? ComparisonResult.Equal : ComparisonResult.GreaterThan;
        } else {
            return ComparisonResult.from(comparison);
        }
    }

    @Override
    public ComparisonResult compareTo(SFractionBigInt other) {
        var divRem1 = numerator.divideAndRemainder(denominator);
        var divRem2 = other.numerator.divideAndRemainder(other.denominator);

        var div1 = divRem1[0];
        var div2 = divRem2[0];
        var rem1 = divRem1[1];
        var rem2 = divRem2[1];

        var comparisonDiv = div1.compareTo(div2);
        if (comparisonDiv == 0) {
            return ComparisonResult.from(rem1.compareTo(rem2));
        } else {
            return ComparisonResult.from(comparisonDiv);
        }
    }

    @Override
    public ComparisonResult compareTo(SFractionLong q) {
        return compareTo(
                q.asBigInt()
        );
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
