package truffle_scheme6.nodes.special;

import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.Constants;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.nodes.atoms.SSymbolLiteralNode;

public class SDefineVarNode extends SSpecialNode {
    @Child
    private SSymbolLiteralNode symbol;
    @Child
    private SchemeNode expr; // nullable

    public SDefineVarNode(SSymbolLiteralNode symbol, SchemeNode expr) {
        this.symbol = symbol;
        this.expr = expr;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        this.getCurrentContext().globalScope.setVar(
                symbol.getSymbol(),
                expr == null ? Constants.UNSPECIFIED : expr.execute(frame));
        return Constants.UNSPECIFIED;
    }

    @Override
    public String toString() {
        return "(define %s %s)".formatted(symbol, expr);
    }
}
