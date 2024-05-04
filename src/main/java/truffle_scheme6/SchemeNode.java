package truffle_scheme6;

import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import truffle_scheme6.nodes.STypes;
import truffle_scheme6.nodes.atoms.SNilLiteralNode;

@TypeSystemReference(STypes.class)
public abstract class SchemeNode extends Node {
    public abstract Object execute(VirtualFrame frame);

    public abstract Object executeFrozen(VirtualFrame frame);

    public boolean isNil() {
        return this instanceof SNilLiteralNode;
    }
}
