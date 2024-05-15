package truffle_scheme6.nodes.functions;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;

public abstract class SReadArgNode extends Node {
    public abstract Object execute(VirtualFrame frame);
}

