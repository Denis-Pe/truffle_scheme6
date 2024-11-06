package truffle_scheme6.nodes.atoms.numbers.integers;

import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.nodes.atoms.numbers.*;

import java.math.BigInteger;

public class SExactFixnumNode extends SIntegerLiteralNode {
    private final long value;

    public SExactFixnumNode(long value) {
        this.value = value;
    }

    @Override
    public BigInteger asBigInteger() {
        return BigInteger.valueOf(value);
    }

    @Override
    public long asLong() {
        return value;
    }

    @Override
    public Long execute(VirtualFrame frame) {
        return value;
    }

    @Override
    public SExactRealNode asExactReal() {
        return new SExactRealNode(value);
    }

    @Override
    public SInexactReal64Node asInexact64() {
        return new SInexactReal64Node(value);
    }

    @Override
    public SInexactReal32Node asInexact32() {
        return new SInexactReal32Node(value);
    }

    @Override
    public boolean isOne() {
        return value == 1L;
    }

    @Override
    public boolean isZero() {
        return value == 0L;
    }

    @Override
    public SFractionLiteralNode asFraction() {
        return new SFractionLiteralNode(
                this,
                new SExactFixnumNode(1)
        );
    }

    @Override
    public SNumberLiteralNode negate() {
        return new SExactFixnumNode(-value);
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
