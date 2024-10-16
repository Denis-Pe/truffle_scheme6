package truffle_scheme6.nodes.special;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;
import org.graalvm.collections.Pair;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.nodes.atoms.SSymbolLiteralNode;

public class SLetNode extends SSpecialNode {
    @NodeChild(value = "valueNode", type = SchemeNode.class)
    @NodeField(name = "slot", type = int.class)
    public static abstract class SLetBindingNode extends SSpecialNode {
        protected abstract int getSlot();

        @Specialization
        protected Object setVar(VirtualFrame frame, Object value) {
            frame.getFrameDescriptor().setSlotKind(getSlot(), FrameSlotKind.Object);

            frame.setObject(getSlot(), value);
            return value;
        }

        public abstract Object execute(VirtualFrame frame);
    }

    @Children
    private SLetBindingNode[] bindings;
    @Children
    private SchemeNode[] body;

    public SLetNode(Pair<SSymbolLiteralNode, SchemeNode>[] bindings, SchemeNode[] body) {
        this.bindings = new SLetBindingNode[bindings.length];
        for (int i = 0; i < bindings.length; i++) {
            var p = bindings[i];
            this.bindings[i] = SLetNodeFactory.SLetBindingNodeGen.create(
                    p.getRight(),
                    ((SSymbolLiteralNode.ReadLocal) p.getLeft().getVarDispatch()).getSlot()
            );
        }

        this.body = body;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        for (var b : bindings) {
            b.execute(frame);
        }

        Object res = null;
        for (var form : body) {
            res = form.execute(frame);
        }

        return res;
    }
}

