package truffle_scheme6.nodes.special;

import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.SchemeNode;

public class SQuoteNode extends SSpecialNode {
    @Child private SchemeNode child;

    public SQuoteNode(SchemeNode child) {
        this.child = child;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        return child.freeze(frame);
    }

    @Override
    public String toString() {
        return "(quote %s)".formatted(child.toString());
    }
}

