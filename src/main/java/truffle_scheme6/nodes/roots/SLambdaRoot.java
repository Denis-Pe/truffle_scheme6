package truffle_scheme6.nodes.roots;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;
import truffle_scheme6.Constants;
import truffle_scheme6.SchemeLanguage;
import truffle_scheme6.SchemeNode;

public class SLambdaRoot extends RootNode {
    @Children
    private final SchemeNode[] nodes;
    private final String name;

    public SLambdaRoot(SchemeLanguage language, FrameDescriptor frameDescriptor, SchemeNode... nodes) {
        super(language, frameDescriptor);
        this.nodes = nodes;
        this.name = "anonymous-lambda@" + System.identityHashCode(this);
    }

    public SLambdaRoot(SchemeLanguage language, FrameDescriptor frameDescriptor, String name, SchemeNode... nodes) {
        super(language, frameDescriptor);
        this.nodes = nodes;
        this.name = name;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        Object res = Constants.UNSPECIFIED;

        for (var n : nodes) {
            res = n.execute(frame);
        }

        return res;
    }

    @Override
    public String toString() {
        return name;
    }
}
