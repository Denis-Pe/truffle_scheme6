package truffle_scheme6.nodes.atoms.numbers;

import com.oracle.truffle.api.frame.VirtualFrame;

public class SComplexLiteralNode extends SNumberLiteralNode {
    @Child
    private SNumberLiteralNode real;
    @Child
    private SNumberLiteralNode imag;

    public SComplexLiteralNode(SNumberLiteralNode real, SNumberLiteralNode imag) {
        this.real = real;
        this.imag = imag;
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
