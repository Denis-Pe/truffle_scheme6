package truffle_scheme6.nodes.composites;

import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.nodes.atoms.numbers.SOctetLiteralNode;
import truffle_scheme6.runtime.SByteVector;
import truffle_scheme6.utils.StringFormatting;

public final class SByteVectorLiteralNode extends SchemeNode {
    // ByteVectors can only contain literal octets
    @Children
    private SOctetLiteralNode[] octets;

    public SByteVectorLiteralNode(SOctetLiteralNode[] octets) {
        this.octets = octets;
    }

    public SByteVectorLiteralNode() {
        this.octets = new SOctetLiteralNode[0];
    }

    @Override
    public Object execute(VirtualFrame frame) {
        byte[] res = new byte[octets.length];

        for (int i = 0; i < octets.length; i++) {
            res[i] = octets[i].execute(frame);
        }

        return new SByteVector(res);
    }

    @Override
    public Object executeFrozen(VirtualFrame frame) {
        return execute(frame);
    }

    @Override
    public String toString() {
        return "#vu8(" +
                StringFormatting.spaced(octets) +
                ')';
    }
}
