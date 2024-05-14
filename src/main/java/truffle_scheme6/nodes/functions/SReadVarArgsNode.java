package truffle_scheme6.nodes.functions;

import com.oracle.truffle.api.frame.VirtualFrame;

public class SReadVarArgsNode extends SReadArgsNode {
    @Override
    public Object[] execute(VirtualFrame frame) {
        return frame.getArguments();
    }
}
