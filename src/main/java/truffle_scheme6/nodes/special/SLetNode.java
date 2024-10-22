package truffle_scheme6.nodes.special;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;
import org.graalvm.collections.Pair;
import truffle_scheme6.Constants;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.nodes.atoms.SSymbolLiteralNode;
import truffle_scheme6.utils.StringFormatting;

import java.util.Arrays;

public class SLetNode extends SSpecialNode {

    @NodeField(name = "slot", type = int.class)
    @NodeField(name = "slotName", type = SSymbolLiteralNode.class)
    @NodeChild(value = "valueNode", type = SchemeNode.class)
    public static abstract class BindingNode extends SSpecialNode {
        protected abstract int getSlot();

        public abstract SSymbolLiteralNode getSlotName();

        public abstract SchemeNode getValueNode();

        protected final static FrameSlotKind BOOLEAN = FrameSlotKind.Boolean;
        protected final static FrameSlotKind LONG = FrameSlotKind.Long;
        protected final static FrameSlotKind FLOAT = FrameSlotKind.Float;
        protected final static FrameSlotKind DOUBLE = FrameSlotKind.Double;
        protected final static FrameSlotKind BYTE = FrameSlotKind.Byte;

        @Specialization(guards = "isKindOrIllegal(frame, BOOLEAN)")
        protected Object setBoolean(VirtualFrame frame, boolean value) {
            frame.getFrameDescriptor().setSlotKind(getSlot(), FrameSlotKind.Boolean);

            frame.setBoolean(getSlot(), value);
            return value;
        }

        @Specialization(guards = "isKindOrIllegal(frame, LONG)")
        protected Object setLong(VirtualFrame frame, long value) {
            frame.getFrameDescriptor().setSlotKind(getSlot(), FrameSlotKind.Long);

            frame.setLong(getSlot(), value);
            return value;
        }

        @Specialization(guards = "isKindOrIllegal(frame, FLOAT)")
        protected Object setFloat(VirtualFrame frame, float value) {
            frame.getFrameDescriptor().setSlotKind(getSlot(), FrameSlotKind.Float);

            frame.setFloat(getSlot(), value);
            return value;
        }

        @Specialization(guards = "isKindOrIllegal(frame, DOUBLE)")
        protected Object setDouble(VirtualFrame frame, double value) {
            frame.getFrameDescriptor().setSlotKind(getSlot(), FrameSlotKind.Double);

            frame.setDouble(getSlot(), value);
            return value;
        }

        @Specialization(guards = "isKindOrIllegal(frame, BYTE)")
        protected Object setByte(VirtualFrame frame, byte value) {
            frame.getFrameDescriptor().setSlotKind(getSlot(), FrameSlotKind.Byte);

            frame.setByte(getSlot(), value);
            return value;
        }

        @Specialization(replaces = {"setBoolean", "setLong", "setFloat", "setDouble", "setByte"})
        protected Object setObject(VirtualFrame frame, Object value) {
            frame.getFrameDescriptor().setSlotKind(getSlot(), FrameSlotKind.Object);

            frame.setObject(getSlot(), value);
            return value;
        }

        protected boolean isKindOrIllegal(VirtualFrame frame, FrameSlotKind primitiveKind) {
            FrameSlotKind kind = frame.getFrameDescriptor().getSlotKind(getSlot());
            return kind == primitiveKind || kind == FrameSlotKind.Illegal;
        }

        public abstract Object execute(VirtualFrame frame);

        @Override
        public String toString() {
            return "(" + this.getSlotName() + " " + this.getValueNode() + ")";
        }
    }

    @Children
    private BindingNode[] bindings;
    @Children
    private SchemeNode[] body;

    public SLetNode(Pair<SSymbolLiteralNode, SchemeNode>[] bindings, SchemeNode[] body) {
        this.bindings = new BindingNode[bindings.length];
        for (int i = 0; i < bindings.length; i++) {
            var p = bindings[i];
            this.bindings[i] = SLetNodeFactory.BindingNodeGen.create(
                    p.getRight(),
                    ((SSymbolLiteralNode.ReadLocal) p.getLeft().getVarDispatch()).getSlot(),
                    p.getLeft()
            );
        }

        this.body = body;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        for (var b : bindings) {
            b.execute(frame);
        }

        Object res = Constants.UNSPECIFIED;
        for (var form : body) {
            res = form.execute(frame);
        }

        return res;
    }

    @Override
    public String toString() {
        return "(let ("
                + StringFormatting.spaced(Arrays.stream(bindings).map(BindingNode::toString).toArray())
                + ") "
                + StringFormatting.spaced(body)
                + ")";
    }
}

