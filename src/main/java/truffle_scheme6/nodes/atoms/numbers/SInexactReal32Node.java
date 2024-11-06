package truffle_scheme6.nodes.atoms.numbers;

import com.oracle.truffle.api.frame.VirtualFrame;

import java.math.BigDecimal;
import java.util.function.Function;

public class SInexactReal32Node extends SNumberLiteralNode {
    private final float value;

    public SInexactReal32Node(float value) {
        this.value = value;
    }

    @Override
    public Float execute(VirtualFrame frame) {
        return value;
    }

    @Override
    public SExactRealNode asExactReal() {
        return new SExactRealNode(new BigDecimal(value));
    }

    @Override
    public SInexactReal64Node asInexact64() {
        return new SInexactReal64Node(value);
    }

    @Override
    public SInexactReal32Node asInexact32() {
        return this;
    }

    @Override
    public boolean isZero() {
        return value == 0.0f;
    }

    @Override
    public SNumberLiteralNode negate() {
        return new SInexactReal32Node(-value);
    }

    @Override
    public SNumberLiteralNode applyExp(int exponent) {
        if (exponent == 0) {
            return this;
        } else {
            Function<Float, Float> operation = switch (Math.round(Math.signum(exponent))) {
                case -1 -> (flo) -> flo / 10.0f;
                case 1 -> (flo) -> flo * 10.0f;
                default -> throw new IllegalStateException("Unreachable code");
            };

            float floatValue = value;

            for (int i = 0; i < Math.abs(exponent); i++) {
                floatValue = operation.apply(floatValue);
            }

            return new SInexactReal32Node(floatValue);
        }
    }

    @Override
    public String toString() {
        return "#i%s".formatted(value);
    }
}
