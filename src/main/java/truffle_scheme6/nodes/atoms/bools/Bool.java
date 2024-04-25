package truffle_scheme6.nodes.atoms.bools;

import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.SchemeObject;

public abstract class Bool extends SchemeObject {
    @Override
    public SchemeObject execute(VirtualFrame frame) {
        return this;
    }

    @Override
    public SchemeObject executeFrozen(VirtualFrame frame) {
        return this;
    }

    @Override
    public String dbg() {
        return this.getClass().getSimpleName();
    }
}
