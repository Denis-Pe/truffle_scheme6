package truffle_scheme6.nodes.atoms.numbers;

import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.SchemeNode;

// you might be wondering why not inherit from SNumberLiteralNode
// reason why is that octets are different from the rest of number nodes
// given that they only occur in byte vectors. Hence, the parser
// doesn't really use the operations SNumberLiteralNode defines
// with octets
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
    public Byte executeFrozen(VirtualFrame frame) {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
