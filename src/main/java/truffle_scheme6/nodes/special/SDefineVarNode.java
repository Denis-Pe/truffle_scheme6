package truffle_scheme6.nodes.special;

import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.Constants;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.nodes.atoms.SSymbolLiteralNode;

import java.util.Objects;

public class SDefineVarNode extends SSpecialNode {
    @Child
    private SSymbolLiteralNode symbol;
    @Child
    private SchemeNode expr; // nullable // TODO Implementation responsibilities:
    //                                        Implementations should detect that the continuation of
    //                                        expression is invoked more than once.
    //                                        If the implementation detects this, it must raise
    //                                        an exception with condition type &assertion.

    public SDefineVarNode(SSymbolLiteralNode symbol, SchemeNode expr) {
        this.symbol = Objects.requireNonNull(symbol);
        this.expr = expr;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        this.getCurrentContext().globalScope.getActiveLibrary().addExport(
                symbol.getSymbol(),
                expr == null ? Constants.UNSPECIFIED : expr.execute(frame));
        return Constants.UNSPECIFIED;
    }

    @Override
    public String toString() {
        return "(define %s %s)".formatted(symbol, expr);
    }
}
