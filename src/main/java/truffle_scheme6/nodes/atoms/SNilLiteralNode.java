package truffle_scheme6.nodes.atoms;

import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.runtime.SNil;

import javax.naming.OperationNotSupportedException;

public final class SNilLiteralNode extends SchemeNode {
    public SNilLiteralNode() {}

    @Override
    public Object execute(VirtualFrame frame) {
        // todo integrate error with Scheme
        throw new UnsupportedOperationException("Nil literals must be quoted");
    }

    @Override
    public Object executeFrozen(VirtualFrame frame) {
        return SNil.SINGLETON;
    }

    @Override
    public String toString() {
        return "()";
    }
}
