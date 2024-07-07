package truffle_scheme6.nodes.atoms.numbers;

import com.oracle.truffle.api.frame.VirtualFrame;

import java.math.BigDecimal;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

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
    public SNumberLiteralNode applyExp(int exponent) {
        Function<BigDecimal, BigDecimal> operation = switch ((int) Math.signum(exponent)) {
            case -1 -> (bd) -> bd.divide(BigDecimal.TEN);
            case 1 -> (bd) -> bd.multiply(BigDecimal.TEN);
            default -> throw new IllegalStateException("Unreachable code");
        };

        if (exponent == 0) {
            return this;
        } else {
            BigDecimal bigValue = value;

            for (int i = 0; i < Math.abs(exponent); i++) {
                bigValue = operation.apply(bigValue);
            }

            return new SExactRealNode(bigValue);
        }
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
