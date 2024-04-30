package truffle_scheme6.nodes.atoms.bools;

import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.SchemeNode;

public final class STrueLiteralNode extends SchemeNode {
    public STrueLiteralNode() {
    }

    @Override
    public Object execute(VirtualFrame frame) {
        return true;
    }

    @Override
    public Object executeFrozen(VirtualFrame frame) {
        return true;
    }

    @Override
    public String toString() {
        return "#T";
    }
}
