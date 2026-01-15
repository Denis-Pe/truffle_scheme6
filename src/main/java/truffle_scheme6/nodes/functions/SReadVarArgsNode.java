package truffle_scheme6.nodes.functions;

import com.oracle.truffle.api.frame.VirtualFrame;

// the argument after is to specify how many arguments
// the arguments array should skip. Useful for functions like
// (lambda (first second . rest) ...)
// where after would be equal to 2 in order for this node to return the `rest`
// of args
public class SReadVarArgsNode extends SReadArgNode {
    private final int after;

    public SReadVarArgsNode(int after) {
        this.after = after;
    }

    @Override
    public Object[] execute(VirtualFrame frame) {
        if (after == 0) {
            return frame.getArguments();
        } else {
            var args = frame.getArguments();
            var res = new Object[Math.max(args.length - after, 0)];

            System.arraycopy(args, after, res, 0, res.length);

            return res;
        }
    }
}
