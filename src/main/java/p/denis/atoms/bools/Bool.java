package p.denis.atoms.bools;

import com.oracle.truffle.api.frame.VirtualFrame;
import p.denis.SchemeObject;

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
