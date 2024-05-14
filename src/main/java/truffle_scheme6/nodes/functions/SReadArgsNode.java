package truffle_scheme6.nodes.functions;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import truffle_scheme6.SchemeNode;

public abstract class SReadArgsNode extends Node {
    public abstract Object[] execute(VirtualFrame frame);
}
