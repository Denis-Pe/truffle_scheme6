package truffle_scheme6.nodes.atoms.bools;

import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.SchemeNode;

public final class SFalseLiteralNode extends SchemeNode {
    public SFalseLiteralNode() {
    }

    @Override
    public Object execute(VirtualFrame frame) {
        return false;
    }

    @Override
    public Object freeze(VirtualFrame frame) {
        return false;
    }

    @Override
    public String toString() {
        return "#F";
    }
}
