package p.denis.composites;

import com.oracle.truffle.api.frame.VirtualFrame;
import p.denis.SchemeObject;
import p.denis.utils.StringFormatting;

import java.util.Arrays;

public final class Vector extends Composite {
    @Children
    private SchemeObject[] elms;

    public Vector(SchemeObject[] elms) {
        this.elms = elms;
    }

    public Vector() {
        this.elms = new SchemeObject[0];
    }

    @Override
    public SchemeObject execute(VirtualFrame frame) {
        for (int i = 0; i < elms.length; i++) {
            elms[i] = elms[i].execute(frame);
        }

        return this;
    }

    @Override
    public SchemeObject executeFrozen(VirtualFrame frame) {
        for (int i = 0; i < elms.length; i++) {
            elms[i] = elms[i].executeFrozen(frame);
        }

        return this;
    }

    @Override
    Iterable<SchemeObject> children() {
        return () -> Arrays.stream(elms).map(o -> (SchemeObject) o).iterator();
    }

    public SchemeObject[] getElms() {
        return elms;
    }

    @Override
    public String toString() {
        return "#(" +
                StringFormatting.spaced(elms) +
                ")";
    }

    @Override
    public String dbg() {
        return "%s{%s}".formatted(
                this.getClass().getSimpleName(),
                StringFormatting.commaSeparated(Arrays.stream(elms).map(SchemeObject::dbg).toArray()));
    }
}
