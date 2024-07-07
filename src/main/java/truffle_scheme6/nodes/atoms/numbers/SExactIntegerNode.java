package truffle_scheme6.nodes.atoms.numbers;

import com.oracle.truffle.api.frame.VirtualFrame;

import java.math.BigInteger;

public class SExactIntegerNode extends SNumberLiteralNode {
    private final BigInteger value;

    public SExactIntegerNode(BigInteger value) {
        this.value = value;
    }

    public SExactIntegerNode(String s, int radix) {
        this.value = new BigInteger(s, radix);
    }

    public SExactIntegerNode(long value) {
        this.value = BigInteger.valueOf(value);
    }

    @Override
    public Object execute(VirtualFrame frame) {
        return value;
    }

    @Override
    public Object executeFrozen(VirtualFrame frame) {
        return value;
    }

    @Override
    public SNumberLiteralNode negate() {
        return new SExactIntegerNode(value.negate());
    }

    @Override
    public SNumberLiteralNode asReal32() {
        return new SInexactReal32Node(value.floatValue());
    }

    @Override
    public SNumberLiteralNode asReal64() {
        return new SInexactReal64Node(value.doubleValue());
    }

    @Override
    public SNumberLiteralNode applyExp(int exponent) {
        throw SNumberLiteralNode.notUsedByParser(this);
    }

    @Override
    public SNumberLiteralNode asExact() {
        return this;
    }

    @Override
    public SNumberLiteralNode asInexact() {
        return new SInexactIntegerNode(value.longValue());
    }

    @Override
    public String toString() {
        return "#e%s".formatted(value.toString());
    }
}
