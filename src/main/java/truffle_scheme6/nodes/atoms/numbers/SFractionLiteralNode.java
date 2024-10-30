package truffle_scheme6.nodes.atoms.numbers;

import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.nodes.atoms.numbers.integers.SExactBigIntegerNode;
import truffle_scheme6.nodes.atoms.numbers.integers.SIntegerLiteralNode;
import truffle_scheme6.runtime.numbers.FractionBigInt;
import truffle_scheme6.runtime.numbers.FractionLong;

public class SFractionLiteralNode extends SNumberLiteralNode {
    private final SIntegerLiteralNode numerator;
    private final SIntegerLiteralNode denominator;

    public SFractionLiteralNode(SIntegerLiteralNode numerator, SIntegerLiteralNode denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        if (numerator instanceof SExactBigIntegerNode || denominator instanceof SExactBigIntegerNode) {
            return new FractionBigInt(numerator.asBigInteger(), denominator.asBigInteger());
        } else {
            return new FractionLong(numerator.asLong(), denominator.asLong());
        }
    }

    @Override
    public Object executeFrozen(VirtualFrame frame) {
        return execute(frame);
    }

    @Override
    public SNumberLiteralNode negate() {
        return new SFractionLiteralNode((SIntegerLiteralNode) numerator.negate(), denominator);
    }

    @Override
    public SNumberLiteralNode applyExp(int exponent) {
        throw SNumberLiteralNode.notUsedByParser(this);
    }

    @Override
    public String toString() {
        return "%s/%s".formatted(numerator, denominator);
    }
}
