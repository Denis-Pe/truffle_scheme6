package truffle_scheme6.nodes.atoms.numbers;

import com.oracle.truffle.api.frame.VirtualFrame;

import java.math.BigDecimal;
import java.util.function.Function;

public class SInexactReal32Node extends SNumberLiteralNode {
    private final float value;

    public SInexactReal32Node(float value) {
        this.value = value;
    }

    public static SInexactReal32Node nan() {
        return new SInexactReal32Node(Float.NaN);
    }

    public static SInexactReal32Node posInf() {
        return new SInexactReal32Node(Float.POSITIVE_INFINITY);
    }

    public static SInexactReal32Node negInf() {
        return new SInexactReal32Node(Float.NEGATIVE_INFINITY);
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
        return new SInexactReal32Node(-value);
    }

    @Override
    public SNumberLiteralNode asReal32() {
        return this;
    }

    @Override
    public SNumberLiteralNode asReal64() {
        return new SInexactReal64Node(value);
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

            return new SExactRealNode(floatValue);
        }
    }

    @Override
    public SNumberLiteralNode asExact() {
        return new SExactRealNode(value);
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
