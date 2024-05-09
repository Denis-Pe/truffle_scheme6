package truffle_scheme6.nodes.special;

import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.Constants;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.nodes.atoms.SIdentifierLiteralNode;
import truffle_scheme6.runtime.SNil;

public class SDefineVarNode extends SSpecialNode {
    @Child
    private SIdentifierLiteralNode identifier;
    @Child
    private SchemeNode expr; // nullable

    public SDefineVarNode(SIdentifierLiteralNode identifier, SchemeNode expr) {
        this.identifier = identifier;
        this.expr = expr;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        this.getCurrentContext().globalScope.setVar(
                identifier.execute(frame),
                expr == null ? Constants.UNSPECIFIED : expr.execute(frame));
        return Constants.UNSPECIFIED;
    }
}
