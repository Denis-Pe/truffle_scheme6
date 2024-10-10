package truffle_scheme6.nodes.special;

import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.Constants;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.runtime.SNil;
import truffle_scheme6.utils.StringFormatting;

import java.util.Arrays;

public class SBeginNode extends SSpecialNode {
    @Children
    private SchemeNode[] nodes;

    public SBeginNode(SchemeNode[] nodes) {
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
        return "(begin %s)".formatted(StringFormatting.spaced(nodes));
    }
}
