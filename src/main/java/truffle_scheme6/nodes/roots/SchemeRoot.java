package truffle_scheme6.nodes.roots;

import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;
import truffle_scheme6.Constants;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.utils.StringFormatting;

import java.util.Arrays;

public class SchemeRoot extends RootNode {
    @Children
    private final SchemeNode[] nodes;

    public SchemeRoot(TruffleLanguage<?> language, FrameDescriptor rootFrameDesc, SchemeNode... nodes) {
        super(language, rootFrameDesc);
        this.nodes = nodes;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        Object res = Constants.UNSPECIFIED;

        for (var node : nodes) {
            res = node.execute(frame);
        }

        return res;
    }

    @Override
    public String toString() {
        return StringFormatting.separatedBy("\n", nodes);
    }
}
