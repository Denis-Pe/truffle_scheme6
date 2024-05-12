package truffle_scheme6.nodes.atoms;

import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.runtime.SSymbol;

import java.util.stream.IntStream;

public final class SSymbolLiteralNode extends SchemeNode {
    private final SSymbol symbol;

    public SSymbolLiteralNode(int[] codepoints) {
        this.symbol = SSymbol.get(codepoints);
    }

    public SSymbolLiteralNode(IntStream codepoints) {
        this(codepoints.toArray());
    }

    public SSymbolLiteralNode(String javaString) {
        this(javaString.codePoints());
    }

    @Override
    public Object execute(VirtualFrame frame) {
        var val = this.getCurrentContext().globalScope.getVar(symbol);
        if (val != null) {
            return val;
        } else {
            throw new RuntimeException("Symbol " + this + " is not bound to a variable");
        }
    }

    @Override
    public SSymbol executeFrozen(VirtualFrame frame) {
        return symbol;
    }

    public SSymbol getValue() {
        return symbol;
    }

    @Override
    public String toString() {
        return symbol.toString();
    }

}
