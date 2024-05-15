package truffle_scheme6.nodes.atoms;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.nodes.functions.SReadArgNode;
import truffle_scheme6.runtime.SSymbol;

import java.util.stream.IntStream;

public final class SSymbolLiteralNode extends SchemeNode {
    // only meant to be used as an arg to SSymbolLiteralNode's constructor
    public abstract static class ReadVarDispatch extends SchemeNode {
        // initialized by parent node
        protected SSymbolLiteralNode parent;

        @Override
        public final Object executeFrozen(VirtualFrame frame) {
            throw new UnsupportedOperationException("Can't freeze a SSymbolLiteralNode.ReadArg node");
        }
    }

    public static class ReadArgDispatch extends ReadVarDispatch {
        private final SReadArgNode argReader;

        public ReadArgDispatch(SReadArgNode argReader) {
            this.argReader = argReader;
        }

        @Override
        public Object execute(VirtualFrame frame) {
            return this.argReader.execute(frame);
        }
    }

    @NodeField(name = "slot", type = int.class)
    public static abstract class ReadLocal extends ReadVarDispatch {
        protected abstract int getSlot();

        @Specialization(guards = "frame.isLong(getSlot())")
        protected long readLong(VirtualFrame frame) {
            return frame.getLong(getSlot());
        }

        @Specialization(guards = "frame.isBoolean(getSlot())")
        protected boolean readBoolean(VirtualFrame frame) {
            return frame.getBoolean(getSlot());
        }

        @Specialization(replaces = {"readLong", "readBoolean"})
        protected Object readObject(VirtualFrame frame) {
            if (!frame.isObject(getSlot())) {
                CompilerDirectives.transferToInterpreter();
                Object result = frame.getValue(getSlot());
                frame.setObject(getSlot(), result);
                return result;
            }

            return frame.getObject(getSlot());
        }

    }

    public static class ReadGlobal extends ReadVarDispatch {
        @Override
        public Object execute(VirtualFrame frame) {
            var val = this.getCurrentContext().globalScope.getVar(parent.symbol);
            if (val != null) {
                return val;
            } else {
                throw new RuntimeException("Symbol " + this + " is not bound");
            }
        }
    }

    private final SSymbol symbol;
    private final ReadVarDispatch varDispatch;

    public SSymbolLiteralNode(int[] codepoints, ReadVarDispatch varDispatch) {
        this.symbol = SSymbol.get(codepoints);
        this.varDispatch = varDispatch;
        this.varDispatch.parent = this;
    }

    public SSymbolLiteralNode(IntStream codepoints, ReadVarDispatch varDispatch) {
        this(codepoints.toArray(), varDispatch);
    }

    public SSymbolLiteralNode(String javaString, ReadVarDispatch varDispatch) {
        this(javaString.codePoints(), varDispatch);
    }

    @Override
    public Object execute(VirtualFrame frame) {
        return varDispatch.execute(frame);
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
