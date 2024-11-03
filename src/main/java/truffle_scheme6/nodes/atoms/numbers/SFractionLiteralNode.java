package truffle_scheme6.nodes.atoms.numbers;

import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.nodes.atoms.numbers.integers.SExactBigIntegerNode;
import truffle_scheme6.nodes.atoms.numbers.integers.SIntegerLiteralNode;
import truffle_scheme6.runtime.numbers.SBigInt;
import truffle_scheme6.runtime.numbers.SFixnum;
import truffle_scheme6.runtime.numbers.SFractionBigInt;
import truffle_scheme6.runtime.numbers.SFractionLong;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public class SFractionLiteralNode extends SNumberLiteralNode {
    private final SIntegerLiteralNode numerator;
    private final SIntegerLiteralNode denominator;

    public SFractionLiteralNode(SIntegerLiteralNode numerator, SIntegerLiteralNode denominator) {
        this.numerator = numerator;
        this.denominator = denominator;

        if (denominator.isZero()) {
            throw new ArithmeticException("denominator cannot be zero");
        }
    }

    @Override
    public Object execute(VirtualFrame frame) {
        if (denominator.isOne()) {
            return numerator.execute(frame);
        } else if (numerator instanceof SExactBigIntegerNode || denominator instanceof SExactBigIntegerNode) {
            var num = numerator.asBigInteger();
            var den = denominator.asBigInteger();

            var divRem = num.divideAndRemainder(den);
            var div = divRem[0];
            var rem = divRem[1];
            if (rem.equals(BigInteger.ZERO)) {
                return new SBigInt(div);
            } else {
                return new SFractionBigInt(numerator.asBigInteger(), denominator.asBigInteger());
            }
        } else {
            var num = numerator.asLong();
            var den = denominator.asLong();

            if (num % den == 0) {
                return new SFixnum(num / den);
            } else {
                return new SFractionLong(numerator.asLong(), denominator.asLong());
            }
        }
    }

    @Override
    public SExactRealNode asExactReal() {
        return new SExactRealNode(new BigDecimal(numerator.asBigInteger()).divide(new BigDecimal(denominator.asBigInteger()), MathContext.DECIMAL128));
    }

    @Override
    public SInexactReal64Node asInexact64() {
        return new SInexactReal64Node(numerator.asBigInteger().doubleValue() / denominator.asBigInteger().doubleValue());
    }

    @Override
    public SInexactReal32Node asInexact32() {
        return new SInexactReal32Node(numerator.asBigInteger().floatValue() / denominator.asBigInteger().floatValue());
    }

    @Override
    public boolean isZero() {
        return numerator.isZero();
    }

    @Override
    public SNumberLiteralNode negate() {
        return new SFractionLiteralNode((SIntegerLiteralNode) numerator.negate(), denominator);
    }

    @Override
    public SNumberLiteralNode applyExp(int exponent) {
        throw SNumberLiteralNode.notUsedByParser(this);
    }

    @Override
    public String toString() {
        return "%s/%s".formatted(numerator, denominator);
    }
}
