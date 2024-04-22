package p.denis;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import p.denis.atoms.Identifier;
import p.denis.atoms.SCharacter;
import p.denis.atoms.SString;
import p.denis.composites.Pair;
import p.denis.composites.Vector;

@NodeChild("args")
public abstract class AdditionNode extends SchemeObject {
//    @Specialization(rewriteOn = ArithmeticException.class)
//    protected InexactInteger addInts()

    @Specialization
    protected SString concat(Pair p) {
        return new SString(((Identifier) p.getCar()).getValue().toString() + ((Identifier) p.getCdr()).getValue().toString());
    }

    @Specialization(replaces = "concat")
    protected SString concatChr(Vector p) {
        return new SString(((SCharacter) p.getElms()[0]).getValue().toString() + ((SCharacter) p.getElms()[1]).getValue().toString());
    }
}
