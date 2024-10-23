package truffle_scheme6.nodes.atoms;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.MaterializedFrame;
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
        public abstract int getSlot();

        @Specialization(guards = "frame.isBoolean(getSlot())")
        protected boolean readBoolean(VirtualFrame frame) {
            return frame.getBoolean(getSlot());
        }

        @Specialization(guards = "frame.isLong(getSlot())")
        protected long readLong(VirtualFrame frame) {
            return frame.getLong(getSlot());
        }

        @Specialization(guards = "frame.isFloat(getSlot())")
        protected float readFloat(VirtualFrame frame) {
            return frame.getFloat(getSlot());
        }

        @Specialization(guards = "frame.isDouble(getSlot())")
        protected double readDouble(VirtualFrame frame) {
            return frame.getDouble(getSlot());
        }

        @Specialization(guards = "frame.isByte(getSlot())")
        protected byte readByte(VirtualFrame frame) {
            return frame.getByte(getSlot());
        }

        @Specialization(replaces = {"readBoolean", "readLong", "readFloat", "readDouble", "readByte"})
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

    @NodeField(name = "rootName", type = String.class)
    // initially null, set during the execution of frame whose name is the field above
    @NodeField(name = "matFrame", type = MaterializedFrame.class)
    @NodeField(name = "slot", type = int.class)
    public static abstract class ReadFromMaterialized extends ReadVarDispatch {
        public abstract String getRootName();

        public abstract MaterializedFrame getMatFrame();

        public abstract void setMatFrame(MaterializedFrame frame);

        public abstract int getSlot();

        @Specialization(guards = "matFrame.isBoolean(getSlot())")
        protected boolean readBoolean(VirtualFrame _vFrame) {
            return getMatFrame().getBoolean(getSlot());
        }

        @Specialization(guards = "matFrame.isLong(getSlot())")
        protected long readLong(VirtualFrame _vFrame) {
            return getMatFrame().getLong(getSlot());
        }

        @Specialization(guards = "matFrame.isFloat(getSlot())")
        protected float readFloat(VirtualFrame _vFrame) {
            return getMatFrame().getFloat(getSlot());
        }

        @Specialization(guards = "matFrame.isDouble(getSlot())")
        protected double readDouble(VirtualFrame _vFrame) {
            return getMatFrame().getDouble(getSlot());
        }

        @Specialization(guards = "matFrame.isByte(getSlot())")
        protected byte readByte(VirtualFrame _vFrame) {
            return getMatFrame().getByte(getSlot());
        }

        @Specialization(replaces = {"readBoolean", "readLong", "readFloat", "readDouble", "readByte"})
        protected Object readObject(VirtualFrame _vFrame) {
            var frame = getMatFrame();
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
                throw new RuntimeException("Symbol " + this.parent + " is not bound");
            }
        }
    }

    private final SSymbol symbol;
    private ReadVarDispatch varDispatch;

    public SSymbolLiteralNode(int[] codepoints) {
        this.symbol = SSymbol.get(codepoints);
    }

    public SSymbolLiteralNode(int[] codepoints, ReadVarDispatch varDispatch) {
        this.symbol = SSymbol.get(codepoints);
        this.varDispatch = varDispatch;
        if (varDispatch != null) {
            this.varDispatch.parent = this;
        }
    }

    public SSymbolLiteralNode(IntStream codepoints, ReadVarDispatch varDispatch) {
        this(codepoints.toArray(), varDispatch);
    }

    public SSymbolLiteralNode(String javaString, ReadVarDispatch varDispatch) {
        this(javaString.codePoints(), varDispatch);
    }

    @Override
    public Object execute(VirtualFrame frame) {
        if (this.varDispatch != null) {
            return varDispatch.execute(frame);
        } else {
            throw new RuntimeException("Symbol " + this + " is not bound");
        }
    }

    @Override
    public SSymbol executeFrozen(VirtualFrame frame) {
        return symbol;
    }

    public SSymbol getSymbol() {
        return symbol;
    }

    public void setVarDispatch(ReadVarDispatch varDispatch) {
        this.varDispatch = varDispatch;
    }

    public ReadVarDispatch getVarDispatch() {
        return varDispatch;
    }

    @Override
    public String toString() {
        return symbol.toString();
    }

}
