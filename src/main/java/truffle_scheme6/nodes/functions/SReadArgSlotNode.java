package truffle_scheme6.nodes.functions;

import com.oracle.truffle.api.frame.VirtualFrame;

public class SReadArgSlotNode extends SReadArgNode {
    private final int slot;

    public SReadArgSlotNode(int slot) {
        this.slot = slot;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        return frame.getArguments()[slot];
    }
}
