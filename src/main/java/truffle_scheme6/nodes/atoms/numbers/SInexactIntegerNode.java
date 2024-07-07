package truffle_scheme6.nodes.atoms.numbers;

import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.SchemeNode;

import java.math.BigInteger;

public class SInexactIntegerNode extends SNumberLiteralNode {
    private final long value;

    public SInexactIntegerNode(long value) {
        this.value = value;
    }

    public static SInexactIntegerNode one() {
        return new SInexactIntegerNode(1);
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
        return new SInexactIntegerNode(-value);
    }

    @Override
    public SNumberLiteralNode asReal32() {
        return new SInexactReal32Node(value);
    }

    @Override
    public SNumberLiteralNode asReal64() {
        return new SInexactReal64Node(value);
    }

    @Override
    public SNumberLiteralNode applyExp(int exponent) {
        throw SNumberLiteralNode.notUsedByParser(this);
    }

    @Override
    public SNumberLiteralNode asExact() {
        return new SExactIntegerNode(value);
    }

    @Override
    public SNumberLiteralNode asInexact() {
        return this;
    }

    @Override
    public String toString() {
        return "#i%s".formatted(value);
    }
}
