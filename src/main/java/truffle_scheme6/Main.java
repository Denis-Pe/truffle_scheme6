package truffle_scheme6;

import truffle_scheme6.nodes.atoms.Identifier;
import truffle_scheme6.nodes.atoms.SCharacter;
import truffle_scheme6.nodes.composites.Pair;
import truffle_scheme6.nodes.composites.Vector;
import truffle_scheme6.nodes.roots.SchemeRoot;

public class Main {
    public static void main(String[] args) throws Exception {
//        var addNode = new SchemeObject() {
//            InexactInteger[] operands = {
//                    new InexactInteger(5),
//                    new InexactInteger(15)
//            };
//
//            @Override
//            public SchemeObject execute(VirtualFrame frame) {
//                return Arrays.stream(operands).reduce((acc, next) -> new InexactInteger(acc.value + next.value)).orElse(InexactInteger.ZERO);
//            }
//
//            @Override
//            public SchemeObject executeFrozen(VirtualFrame frame) {
//                return this.executeFrozen(frame);e
//            }
//
//            @Override
//            public String dbg() {
//                return "elena";
//            }
//        };
//
//        var r = new SchemeRoot(null, addNode);
//        CallTarget ct = r.getCallTarget();
//
//        System.out.println(ct.call());

        Pair identifiers = new Pair(new Identifier("su"), new Identifier("p"));
        Vector vecky = new Vector(new SchemeObject[]{new Identifier("howd"), new Identifier("er")});
        Pair chrs = new Pair(new SCharacter('s'), new SCharacter('u'));

        SchemeObject node = AdditionNodeGen.create(identifiers);
        SchemeRoot r = new SchemeRoot(null, node);

        System.out.println(r.getCallTarget().call());
    }
}