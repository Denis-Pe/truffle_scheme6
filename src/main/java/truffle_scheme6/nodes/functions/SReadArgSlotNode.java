package truffle_scheme6.nodes.functions;

import com.oracle.truffle.api.frame.VirtualFrame;

public class SReadArgSlotNode extends SReadArgNode {
    private final int slot;

    public SReadArgSlotNode(int slot) {
        this.slot = slot;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        var args = frame.getArguments();
        if (args.length <= slot) {
            throw new RuntimeException("Not enough arguments passed to function");
        }

        return args[slot];
    }
}
