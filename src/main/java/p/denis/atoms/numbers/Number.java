package p.denis.atoms.numbers;

import com.oracle.truffle.api.frame.VirtualFrame;
import p.denis.SchemeObject;

public abstract class Number extends SchemeObject {
    @Override
    public SchemeObject execute(VirtualFrame frame) {
        return this;
    }

    @Override
    public SchemeObject executeFrozen(VirtualFrame frame) {
        return this;
    }

    public abstract Number asExact();

    public abstract Number asInexact();

    @Override
    public String dbg() {
        return "%s{%s}".formatted(this.getClass().getSimpleName(), this.toString());
    }
}
