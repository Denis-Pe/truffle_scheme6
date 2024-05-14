package truffle_scheme6.nodes.roots;

import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;
import truffle_scheme6.Constants;
import truffle_scheme6.SchemeNode;

public class SLambdaRoot extends RootNode {
    @Children
    private final SchemeNode[] nodes;

    public SLambdaRoot(TruffleLanguage<?> language, SchemeNode... nodes) {
        super(language);
        this.nodes = nodes;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        Object res = Constants.UNSPECIFIED;

        for (var n : nodes) {
            res = n.execute(frame);
        }

        return res;
    }
}
