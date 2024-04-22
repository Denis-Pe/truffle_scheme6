package p.denis.composites;

import com.oracle.truffle.api.frame.VirtualFrame;
import p.denis.SchemeObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Pair extends Composite {
    @Child
    private SchemeObject car;
    @Child
    private SchemeObject cdr;

    public Pair(SchemeObject car, SchemeObject cdr) {
        this.car = car;
        this.cdr = cdr;
    }

    @Override
    public SchemeObject execute(VirtualFrame frame) {
        return this;
//        if (car instanceof Identifier) {
//            return null;
//        } else {
//            return null;
//        }
    }

    @Override
    public SchemeObject executeFrozen(VirtualFrame frame) {
        car = car.executeFrozen(frame);
        cdr = cdr.executeFrozen(frame);
        return this;
    }

    @Override
    Iterable<SchemeObject> children() {
        return cdr.isNil() ?
                () -> Stream.of(car).map(o -> (SchemeObject) o).iterator() :
                () -> Stream.of(car, cdr).map(o -> (SchemeObject) o).iterator();
    }

    public List<SchemeObject> asList() {
        List<SchemeObject> objs = new ArrayList<>();

        var currPair = this;

        while (true) {
            objs.add(currPair.car);

            if (currPair.cdr instanceof Pair) {
                currPair = (Pair) currPair.cdr;
            } else if (!currPair.cdr.isNil()) {
                objs.add(currPair.cdr);
                break;
            } else {
                break;
            }
        }


        return objs;
    }

    public SchemeObject getCar() {
        return car;
    }

    public SchemeObject getCdr() {
        return cdr;
    }

    @Override
    public String toString() {
        return "(" +
                car +
                " . " +
                cdr +
                ')';
    }

    @Override
    public String dbg() {
        return "%s{%s . %s}".formatted(this.getClass().getSimpleName(), car.dbg(), cdr.dbg());
    }
}
