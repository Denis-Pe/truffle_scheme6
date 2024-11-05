package truffle_scheme6.nodes.atoms.numbers;

import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.SchemeNode;

public class SOctetLiteralNode extends SchemeNode {
    // unsigned byte
    private final byte value;

    public SOctetLiteralNode(byte value) {
        this.value = value;
    }

    @Override
    public Byte execute(VirtualFrame frame) {
        return value;
    }

    @Override
    public Byte freeze(VirtualFrame frame) {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
