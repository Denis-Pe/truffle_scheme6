package truffle_scheme6.nodes.atoms;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.strings.TruffleString;
import com.oracle.truffle.api.strings.TruffleStringBuilder;
import truffle_scheme6.Constants;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.runtime.SChar;

public class SCharacterLiteralNode extends SchemeNode {
    private final int value;

    public SCharacterLiteralNode(char c) {
        this((int) c);
    }

    public SCharacterLiteralNode(int unsignedCodepointInt) {
        this.value = unsignedCodepointInt;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        return new SChar(value);
    }

    @Override
    public Object freeze(VirtualFrame frame) {
        return new SChar(value);
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "#\\" + Character.toString(value);
    }

}
