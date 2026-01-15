package truffle_scheme6.nodes.functions;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import truffle_scheme6.SchemeNode;

public abstract class SReadArgNode extends SchemeNode {
    public abstract Object execute(VirtualFrame frame);

    @Override
    public final Object freeze(VirtualFrame frame) {
        throw new UnsupportedOperationException("Cannot freeze read argument node");
    }
}

