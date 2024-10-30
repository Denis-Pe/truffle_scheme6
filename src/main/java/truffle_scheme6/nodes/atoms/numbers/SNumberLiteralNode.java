package truffle_scheme6.nodes.atoms.numbers;

import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.SchemeNode;

public abstract class SNumberLiteralNode extends SchemeNode {
    public abstract SNumberLiteralNode negate();

    public abstract SNumberLiteralNode applyExp(int exponent);

    // self-evaluating objects
    @Override
    public final Object executeFrozen(VirtualFrame frame) {
        return execute(frame);
    }
    
    // used for operations that are not meant to be used on some of these nodes, like
    // applyTo on Complex numbers. Even though you can mathematically do it, it is not something
    // done by my parser and hence something I won't bother implementing
    protected static UnsupportedOperationException notUsedByParser(SNumberLiteralNode node) {
        return new UnsupportedOperationException(
                "Operation performed not meant to be done on %s".formatted(node.getClass().getSimpleName())
        );
    }
}
