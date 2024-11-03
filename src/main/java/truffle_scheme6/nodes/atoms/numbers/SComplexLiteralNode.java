package truffle_scheme6.nodes.atoms.numbers;

import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.nodes.atoms.numbers.integers.SExactBigIntegerNode;
import truffle_scheme6.nodes.atoms.numbers.integers.SExactFixnumNode;
import truffle_scheme6.runtime.numbers.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Function;

public class SComplexLiteralNode extends SNumberLiteralNode {
    @Child
    private SNumberLiteralNode real;
    @Child
    private SNumberLiteralNode imag;

    private enum Type {
        Float,
        Double,
        BigDecimal,
        Rational
    }

    private static Type biggestTypeNeeded(SNumberLiteralNode real, SNumberLiteralNode imag) {
        Function<Class<?>, Boolean> instance = (c) -> c.isInstance(real) || c.isInstance(imag);

        if (instance.apply(SExactRealNode.class)) {
            return Type.BigDecimal;
        } else if (instance.apply(SInexactReal64Node.class)) {
            return Type.Double;
        } else if (instance.apply(SInexactReal32Node.class)) {
            return Type.Float;
        } else if (instance.apply(SExactBigIntegerNode.class)) {
            return Type.Rational;
        } else if (instance.apply(SExactFixnumNode.class)) {
            return Type.Rational;
        } else {
            throw new IllegalArgumentException("Unsupported types: " + real.getClass() + ", " + imag.getClass());
        }
    }

    public SComplexLiteralNode(SNumberLiteralNode real, SNumberLiteralNode imag) {
        this.real = real;
        this.imag = imag;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        if (imag.isZero()) {
            return real.execute(frame);
        } else {
            return switch (biggestTypeNeeded(real, imag)) {
                case BigDecimal ->
                        new SComplexBigDec(real.asExactReal().execute(frame), imag.asExactReal().execute(frame));
                case Double ->
                        new SComplexDouble((double) real.asInexact64().execute(frame), (double) imag.asInexact64().execute(frame));
                case Float ->
                        new SComplexFloat((float) real.asInexact32().execute(frame), (float) imag.asInexact32().execute(frame));
                case Rational -> new SComplexRational((SRational) real.execute(frame), (SRational) imag.execute(frame));
            };
        }
    }

    @Override
    public boolean isZero() {
        return real.isZero() && imag.isZero();
    }

    @Override
    public SNumberLiteralNode negate() {
        return new SComplexLiteralNode(real.negate(), imag.negate());
    }

    @Override
    public SNumberLiteralNode applyExp(int exponent) {
        throw SNumberLiteralNode.notUsedByParser(this);
    }

    @Override
    public String toString() {
        return "%s + (%s)i".formatted(real, imag);
    }
}
