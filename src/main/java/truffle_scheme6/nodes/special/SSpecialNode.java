package truffle_scheme6.nodes.special;

import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.SchemeNode;

public abstract class SSpecialNode extends SchemeNode {
    @Override
    public final Object executeFrozen(VirtualFrame frame) {
        throw new UnsupportedOperationException("Can't call executeFrozen on a special node");
    }
}
