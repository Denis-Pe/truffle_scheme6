package truffle_scheme6.nodes.functions;

import com.oracle.truffle.api.frame.VirtualFrame;

// the case where a function takes a set number of arguments +
// any optional extra arguments, ala
// (lambda (first second third . rest) ...)
public class SReadMinVarArgsNode extends SReadArgsNode {
    private final int minArgs;

    public SReadMinVarArgsNode(int minArgs) {
        this.minArgs = minArgs;
    }

    @Override
    public Object[] execute(VirtualFrame frame) {
        Object[] frameArgs = frame.getArguments();
        if (frameArgs.length < minArgs) {
            throw new RuntimeException("Not enough arguments passed to function");
        } else {
            return frameArgs;
        }
    }
}
