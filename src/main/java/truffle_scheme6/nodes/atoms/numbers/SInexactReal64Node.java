package truffle_scheme6.nodes.atoms.numbers;

import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.nodes.atoms.numbers.integers.SExactBigIntegerNode;
import truffle_scheme6.nodes.atoms.numbers.integers.SExactFixnumNode;

import java.math.BigInteger;
import java.util.function.Function;

public class SInexactReal64Node extends SNumberLiteralNode {
    private final double value;

    public SInexactReal64Node(double value) {
        this.value = value;
    }

    public static SInexactReal64Node nan() {
        return new SInexactReal64Node(Double.NaN);
    }

    public static SInexactReal64Node posInf() {
        return new SInexactReal64Node(Double.POSITIVE_INFINITY);
    }

    public static SInexactReal64Node negInf() {
        return new SInexactReal64Node(Double.NEGATIVE_INFINITY);
    }

    @Override
    public Double execute(VirtualFrame frame) {
        return value;
    }

    @Override
    public SExactRealNode asExactReal() {
        return new SExactRealNode(value);
    }

    @Override
    public SInexactReal64Node asInexact64() {
        return this;
    }

    @Override
    public boolean isZero() {
        return value == 0.0;
    }

    @Override
    public SNumberLiteralNode negate() {
        return new SInexactReal64Node(-value);
    }

    @Override
    public SNumberLiteralNode applyExp(int exponent) {
        if (exponent == 0) {
            return this;
        } else {
            Function<Double, Double> operation = switch (Math.round(Math.signum(exponent))) {
                case -1 -> (dob) -> dob / 10.0;
                case 1 -> (dob) -> dob * 10.0;
                default -> throw new IllegalStateException("Unreachable code");
            };

            double doubleValue = value;

            for (int i = 0; i < Math.abs(exponent); i++) {
                doubleValue = operation.apply(doubleValue);
            }

            return new SInexactReal64Node(doubleValue);
        }
    }

    @Override
    public String toString() {
        return "#i%s".formatted(value);
    }
}
