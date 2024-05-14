package truffle_scheme6.nodes.functions;

import com.oracle.truffle.api.frame.VirtualFrame;

public class SReadSetArgsNode extends SReadArgsNode {
    private final int exactArgs;

    public SReadSetArgsNode(int exactArgs) {
        this.exactArgs = exactArgs;
    }

    @Override
    public Object[] execute(VirtualFrame frame) {
        Object[] frameArgs = frame.getArguments();

        if (frameArgs.length != exactArgs) {
            throw new RuntimeException("Wrong number of arguments passed to function");
        } else {
            return frameArgs;
        }
    }
}
