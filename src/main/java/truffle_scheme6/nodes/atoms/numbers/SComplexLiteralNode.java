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
    public SNumberLiteralNode asReal32() {
        throw SNumberLiteralNode.notUsedByParser(this);
    }

    @Override
    public SNumberLiteralNode asReal64() {
        throw SNumberLiteralNode.notUsedByParser(this);
    }

    @Override
    public SNumberLiteralNode pow(int exponent) {
        throw SNumberLiteralNode.notUsedByParser(this);
    }

    @Override
    public SNumberLiteralNode asExact() {
        return new SComplexLiteralNode(real.asExact(), imag.asExact());
    }

    @Override
    public SNumberLiteralNode asInexact() {
        return new SComplexLiteralNode(real.asInexact(), imag.asInexact());
    }

    @Override
    public String toString() {
        return "%s + (%s)i".formatted(real, imag);
    }
}
