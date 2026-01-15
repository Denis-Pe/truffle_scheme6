package truffle_scheme6.nodes.composites;

import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.runtime.SVector;
import truffle_scheme6.utils.StringFormatting;

public final class SVectorLiteralNode extends SchemeNode {
    @Children
    private SchemeNode[] elms;

    public SVectorLiteralNode(SchemeNode[] elms) {
        this.elms = elms;
    }

    public SVectorLiteralNode() {
        this.elms = new SchemeNode[0];
    }

    @Override
    public Object execute(VirtualFrame frame) {
        var res = new Object[elms.length];

        for (int i = 0; i < elms.length; i++) {
            res[i] = elms[i].execute(frame);
        }

        return new SVector(res);
    }

    @Override
    public Object freeze(VirtualFrame frame) {
        var res = new Object[elms.length];

        for (int i = 0; i < elms.length; i++) {
            res[i] = elms[i].freeze(frame);
        }

        return new SVector(res);
    }

    @Override
    public String toString() {
        return "#(" +
                StringFormatting.spaced(elms) +
                ")";
    }
}
