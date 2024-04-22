package p.denis.atoms;

import com.oracle.truffle.api.frame.VirtualFrame;
import p.denis.SchemeObject;

// a.k.a. the empty list
// class is named Nil for brevity
public final class Nil extends SchemeObject {
    // S for Singleton
    public static final Nil S = new Nil();

    private Nil() {}

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
