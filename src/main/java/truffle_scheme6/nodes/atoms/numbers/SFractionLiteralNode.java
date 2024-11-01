package truffle_scheme6.nodes.atoms.numbers;

import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.nodes.atoms.numbers.integers.SExactBigIntegerNode;
import truffle_scheme6.nodes.atoms.numbers.integers.SIntegerLiteralNode;
import truffle_scheme6.runtime.numbers.SFractionBigInt;
import truffle_scheme6.runtime.numbers.SFractionLong;

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
            return new SFractionBigInt(numerator.asBigInteger(), denominator.asBigInteger());
        } else {
            return new SFractionLong(numerator.asLong(), denominator.asLong());
        }
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
