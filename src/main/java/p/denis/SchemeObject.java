package p.denis;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import p.denis.atoms.Nil;

public abstract class SchemeObject extends Node {
    public abstract SchemeObject execute(VirtualFrame frame);

    public abstract SchemeObject executeFrozen(VirtualFrame frame);

    public boolean isNil() {
        return this instanceof Nil;
    }

    public String dbg() { return this.toString(); };
}
