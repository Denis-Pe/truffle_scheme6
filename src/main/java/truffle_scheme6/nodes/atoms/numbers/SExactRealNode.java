package truffle_scheme6.nodes.atoms.numbers;

import com.oracle.truffle.api.frame.VirtualFrame;

import java.math.BigDecimal;

public class SExactRealNode extends SNumberLiteralNode {
    private final BigDecimal value;

    public SExactRealNode(BigDecimal value) {
        this.value = value;
    }

    public SExactRealNode(double value) {
        this.value = new BigDecimal(value);
    }

    @Override
    public SNumberLiteralNode negate() {
        return new SExactRealNode(value.negate());
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
    public SNumberLiteralNode pow(int exponent) {
        return new SExactRealNode(value.pow(exponent));
    }

    @Override
    public SNumberLiteralNode asExact() {
        return this;
    }

    @Override
    public SNumberLiteralNode asInexact() {
        return new SInexactReal64Node(value.doubleValue());
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
    public String toString() {
        return "#e%s".formatted(value.toString());
    }
}
