package truffle_scheme6.nodes.special;

import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.Constants;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.nodes.atoms.SSymbolLiteralNode;

public class SSetGlobalNode extends SSpecialNode {
    @Child
    private SSymbolLiteralNode identifier;
    @Child
    private SchemeNode value;

    public SSetGlobalNode(SSymbolLiteralNode identifier, SchemeNode value) {
        this.identifier = identifier;
        this.value = value;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        this.getCurrentContext().globalScope.setVar(
                identifier.getSymbol(),
                value.execute(frame)
        );

        return Constants.UNSPECIFIED;
    }

    @Override
    public String toString() {
        return "(set! %s %s)".formatted(identifier, value);
    }
}
