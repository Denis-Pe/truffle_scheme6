package truffle_scheme6.nodes.special;

import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.Constants;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.nodes.atoms.SSymbolLiteralNode;

import java.util.Objects;

public class SSetLocalNode extends SSpecialNode {
    @Child
    private SLetNode.BindingNode bindingNode;

    public SSetLocalNode(SSymbolLiteralNode identifier, SchemeNode value) {
        Objects.requireNonNull(identifier);
        Objects.requireNonNull(value);
        // given that this node is reused by the closure one 
        var dispatch = identifier.getVarDispatch();
        int slot = switch (dispatch) {
            case SSymbolLiteralNode.ReadLocal readLocal -> readLocal.getSlot();
            case SSymbolLiteralNode.ReadFromMaterialized readFromMaterialized ->
                    readFromMaterialized.getReadLocalNode().getSlot();
            default -> throw new IllegalStateException("Unexpected value: " + dispatch.getClass());
        };

        this.bindingNode = SLetNodeFactory.BindingNodeGen.create(
                value,
                slot,
                identifier
        );
    }

    @Override
    public Object execute(VirtualFrame frame) {
        bindingNode.execute(frame);
        return Constants.UNSPECIFIED;
    }

    @Override
    public String toString() {
        return "(set! %s %s)".formatted(bindingNode.getSlotName(), bindingNode.getValueNode());
    }
}
