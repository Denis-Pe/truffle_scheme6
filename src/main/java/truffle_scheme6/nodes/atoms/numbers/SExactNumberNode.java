package truffle_scheme6.nodes.atoms.numbers;

import com.oracle.truffle.api.frame.VirtualFrame;

import java.math.BigDecimal;
import java.util.function.Function;

public class SExactNumberNode extends SNumberLiteralNode {
    private final BigDecimal value;

    public SExactNumberNode(BigDecimal value) {
        this.value = value;
    }

    public SExactNumberNode(double value) {
        this.value = new BigDecimal(value);
    }

    public SExactNumberNode(long value) {
        this.value = new BigDecimal(value);
    }

    @Override
    public SNumberLiteralNode negate() {
        return new SExactNumberNode(value.negate());
    }

    @Override
    public SNumberLiteralNode applyExp(int exponent) {
        if (exponent == 0) {
            return this;
        } else {
            Function<BigDecimal, BigDecimal> operation = switch (Math.round(Math.signum(exponent))) {
                case -1 -> (bd) -> bd.divide(BigDecimal.TEN);
                case 1 -> (bd) -> bd.multiply(BigDecimal.TEN);
                default -> throw new IllegalStateException("Unreachable code");
            };

            BigDecimal bigValue = value;

            for (int i = 0; i < Math.abs(exponent); i++) {
                bigValue = operation.apply(bigValue);
            }

            return new SExactNumberNode(bigValue);
        }
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
