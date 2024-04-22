package p.denis.composites;

import com.oracle.truffle.api.frame.VirtualFrame;
import p.denis.SchemeObject;
import p.denis.atoms.numbers.Octet;
import p.denis.utils.StringFormatting;

import java.util.Arrays;

public final class ByteVector extends Composite {
    @Children
    private Octet[] octets;

    public ByteVector(Octet[] octets) {
        this.octets = octets;
    }

    public ByteVector() {
        this.octets = new Octet[0];
    }

    @Override
    public SchemeObject execute(VirtualFrame frame) {
        return this;
    }

    @Override
    public SchemeObject executeFrozen(VirtualFrame frame) {
        return this;
    }

    @Override
    Iterable<SchemeObject> children() {
        return () -> Arrays.stream(octets).map(o -> (SchemeObject) o).iterator();
    }

    @Override
    public String toString() {
        return "#vu8(" +
                StringFormatting.spaced(octets) +
                ')';
    }

    @Override
    public String dbg() {
        return "%s{%s}".formatted(
                this.getClass().getSimpleName(),
                StringFormatting.commaSeparated(Arrays.stream(octets).map(SchemeObject::dbg).toArray()));
    }
}
