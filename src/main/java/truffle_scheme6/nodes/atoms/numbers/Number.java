package truffle_scheme6.nodes.atoms.numbers;

import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.SchemeObject;

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
