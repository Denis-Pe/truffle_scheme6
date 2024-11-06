package truffle_scheme6.nodes.atoms.numbers.integers;

import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.nodes.atoms.numbers.*;

import java.math.BigDecimal;
import java.math.BigInteger;

public class SExactBigIntegerNode extends SIntegerLiteralNode {
    private final BigInteger value;

    public SExactBigIntegerNode(BigInteger value) {
        this.value = value;
    }

    @Override
    public BigInteger asBigInteger() {
        return value;
    }

    @Override
    public long asLong() {
        return value.longValue();
    }

    @Override
    public BigInteger execute(VirtualFrame frame) {
        return value;
    }

    @Override
    public SExactRealNode asExactReal() {
        return new SExactRealNode(new BigDecimal(value));
    }

    @Override
    public SInexactReal64Node asInexact64() {
        return new SInexactReal64Node(value.doubleValue());
    }

    @Override
    public SInexactReal32Node asInexact32() {
        return new SInexactReal32Node(value.floatValue());
    }

    @Override
    public boolean isOne() {
        return value.equals(BigInteger.ONE);
    }

    @Override
    public boolean isZero() {
        return value.equals(BigInteger.ZERO);
    }

    @Override
    public SFractionLiteralNode asFraction() {
        return new SFractionLiteralNode(
                this,
                new SExactBigIntegerNode(BigInteger.ONE)
        );
    }

    @Override
    public SNumberLiteralNode negate() {
        return new SExactBigIntegerNode(value.negate());
    }

    @Override
    public SNumberLiteralNode applyExp(int exponent) {
        throw SNumberLiteralNode.notUsedByParser(this);
    }

    @Override
    public String toString() {
        return "#e%s".formatted(value);
    }
}
