package truffle_scheme6;

import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import truffle_scheme6.nodes.STypes;

@TypeSystemReference(STypes.class)
public abstract class SchemeNode extends Node {
    private boolean isTail = false;

    public abstract Object execute(VirtualFrame frame);

    public abstract Object executeFrozen(VirtualFrame frame);

    public boolean isTail() {
        return isTail;
    }

    public void setIsTail() {
        isTail = true; 
    }

    public SchemeLanguageContext getCurrentContext() {
        return SchemeLanguageContext.get(this);
    }
}
