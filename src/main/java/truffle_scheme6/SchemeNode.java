package truffle_scheme6;

import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import truffle_scheme6.nodes.STypes;
import truffle_scheme6.nodes.STypesGen;

@NodeInfo(language = "R6RS Scheme")
@TypeSystemReference(STypes.class)
public abstract class SchemeNode extends Node {
    private boolean isTail = false;

    public abstract Object execute(VirtualFrame frame);

    public boolean executeBoolean(VirtualFrame frame) throws UnexpectedResultException {
        return STypesGen.expectBoolean(execute(frame));
    }

    public byte executeByte(VirtualFrame frame) throws UnexpectedResultException {
        return STypesGen.expectByte(execute(frame));
    }
    
    public long executeLong(VirtualFrame frame) throws UnexpectedResultException {
        return STypesGen.expectLong(execute(frame));
    }

    public float executeFloat(VirtualFrame frame) throws UnexpectedResultException {
        return STypesGen.expectFloat(execute(frame));
    }

    public double executeDouble(VirtualFrame frame) throws UnexpectedResultException {
        return STypesGen.expectDouble(execute(frame));
    }

    public abstract Object freeze(VirtualFrame frame);

    public boolean isTail() {
        return isTail;
    }

    public void setIsTail() {
        isTail = true;
    }

    public SchemeLanguageContext getCurrentContext() {
        return SchemeLanguageContext.get(this);
    }
}
