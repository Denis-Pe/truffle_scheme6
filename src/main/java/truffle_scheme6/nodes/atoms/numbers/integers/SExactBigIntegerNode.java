package truffle_scheme6.nodes.atoms.numbers.integers;

import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.nodes.atoms.numbers.SNumberLiteralNode;

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
