package truffle_scheme6.nodes.special;

import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.runtime.SIdentifier;
import truffle_scheme6.runtime.SNil;
import truffle_scheme6.runtime.SPair;

public class SQuoteNode extends SchemeNode {
    @Child private SchemeNode child;

    public SQuoteNode(SchemeNode child) {
        this.child = child;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        return child.executeFrozen(frame);
    }

    @Override
    public Object executeFrozen(VirtualFrame frame) {
        return new SPair(new SIdentifier("quote"),
                new SPair(child.executeFrozen(frame),
                        SNil.SINGLETON));
    }

    @Override
    public String toString() {
        return "(quote %s)".formatted(child.toString());
    }
}

