package truffle_scheme6;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import truffle_scheme6.nodes.atoms.Identifier;
import truffle_scheme6.nodes.atoms.SCharacter;
import truffle_scheme6.nodes.atoms.SString;
import truffle_scheme6.nodes.composites.Pair;
import truffle_scheme6.nodes.composites.Vector;

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
