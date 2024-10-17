package truffle_scheme6.nodes.atoms.numbers;

import com.oracle.truffle.api.frame.VirtualFrame;

public class SFractionLiteralNode extends SNumberLiteralNode {
    private final SNumberLiteralNode numerator;
    private final SNumberLiteralNode denominator;

    public SFractionLiteralNode(SNumberLiteralNode numerator, SNumberLiteralNode denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        // todo
        return null;
    }

    @Override
    public Object executeFrozen(VirtualFrame frame) {
        // todo
        return null;
    }

    @Override
    public SNumberLiteralNode negate() {
        return new SFractionLiteralNode(numerator.negate(), denominator);
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
