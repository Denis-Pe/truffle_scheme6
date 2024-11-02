package truffle_scheme6.nodes.atoms.numbers;

import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.SchemeNode;

public abstract class SNumberLiteralNode extends SchemeNode {
    public abstract SNumberLiteralNode negate();

    public abstract SNumberLiteralNode applyExp(int exponent);

    // the following 5 methods are used by complex numbers and for a given number,
    // the only implementations needed are the ones for numbers of the same size or bigger
    // where the fixnum is the smallest and the exact real is the biggest
    public SExactRealNode asExactReal() {
        throw notUsedByParser(this);
    }

    public SInexactReal64Node asInexact64() {
        throw notUsedByParser(this);
    }

    public SInexactReal32Node asInexact32() {
        throw notUsedByParser(this);
    }
    
    public SExactBigIntegerNode asExactBigInt() {
        throw notUsedByParser(this);
    }
    
    public SExactFixnumNode asExactFixnum() {
        throw notUsedByParser(this);
    }

    // self-evaluating objects
    @Override
    public final Object executeFrozen(VirtualFrame frame) {
        return execute(frame);
    }

    // used for operations I don't use on some nodes
    protected static UnsupportedOperationException notUsedByParser(SNumberLiteralNode node) {
        return new UnsupportedOperationException(
                "Operation performed not meant to be done on %s".formatted(node.getClass().getSimpleName())
        );
    }
}
