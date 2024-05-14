package truffle_scheme6.nodes.functions;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;

public class SReadArgNode extends Node {
    private final int slot;

    public SReadArgNode(int slot) {
        this.slot = slot;
    }

    public Object execute(VirtualFrame frame) {
        var args = frame.getArguments();
        if (args.length <= slot) {
            throw new RuntimeException("Not enough arguments passed to function");
        }
        
        return args[slot];
    }
}
