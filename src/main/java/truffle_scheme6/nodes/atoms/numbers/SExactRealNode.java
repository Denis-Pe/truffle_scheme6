package truffle_scheme6.nodes.atoms.numbers;

import com.oracle.truffle.api.frame.VirtualFrame;

import java.math.BigDecimal;
import java.util.function.Function;

public class SExactRealNode extends SNumberLiteralNode {
    private final BigDecimal value;

    public SExactRealNode(BigDecimal value) {
        this.value = value;
    }

    public SExactRealNode(double value) {
        this.value = new BigDecimal(value);
    }

    public SExactRealNode(long value) {
        this.value = new BigDecimal(value);
    }

    @Override
    public SExactRealNode asExactReal() {
        return this;
    }

    @Override
    public SNumberLiteralNode negate() {
        return new SExactRealNode(value.negate());
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

            return new SExactRealNode(bigValue);
        }
    }

    @Override
    public BigDecimal execute(VirtualFrame frame) {
        return value;
    }

    @Override
    public String toString() {
        return "#e%s".formatted(value.toString());
    }
}
