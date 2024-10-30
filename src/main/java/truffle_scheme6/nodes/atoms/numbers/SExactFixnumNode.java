package truffle_scheme6.nodes.atoms.numbers;

import com.oracle.truffle.api.frame.VirtualFrame;

public class SExactFixnumNode extends SNumberLiteralNode {
    private final long value;

    public SExactFixnumNode(long value) {
        this.value = value;
    }

    @Override
    public Long execute(VirtualFrame frame) {
        return value;
    }

    @Override
    public Long executeFrozen(VirtualFrame frame) {
        return value;
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
