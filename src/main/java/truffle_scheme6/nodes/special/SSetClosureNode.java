package truffle_scheme6.nodes.special;

import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.nodes.MaterializedFrameUser;
import truffle_scheme6.nodes.atoms.SSymbolLiteralNode;

import java.util.Objects;

public class SSetClosureNode extends SSpecialNode implements MaterializedFrameUser {
    @Child
    private SSetLocalNode localNode;
    private MaterializedFrame materializedFrame;
    private final String rootName;

    public SSetClosureNode(String rootName, SSymbolLiteralNode identifier, SchemeNode value) {
        this.localNode = new SSetLocalNode(Objects.requireNonNull(identifier), Objects.requireNonNull(value));
        this.materializedFrame = null;
        this.rootName = Objects.requireNonNull(rootName);
    }

    @Override
    public Object execute(VirtualFrame _frame) {
        return localNode.execute(materializedFrame);
    }

    @Override
    public String getRootName() {
        return rootName;
    }

    @Override
    public MaterializedFrame getMaterializedFrame() {
        return materializedFrame;
    }

    @Override
    public void setMaterializedFrame(MaterializedFrame materializedFrame) {
        this.materializedFrame = materializedFrame;
    }

    @Override
    public final String toString() {
        return localNode.toString();
    }
}
