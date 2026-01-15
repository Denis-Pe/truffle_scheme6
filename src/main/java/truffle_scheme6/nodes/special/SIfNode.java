package truffle_scheme6.nodes.special;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.profiles.ConditionProfile;
import truffle_scheme6.Constants;
import truffle_scheme6.SchemeNode;

import java.util.Objects;

public class SIfNode extends SSpecialNode {
    @Child
    private SchemeNode conditionNode;
    @Child
    private SchemeNode thenNode;
    @Child
    private SchemeNode elseNode;
    private final ConditionProfile condition = ConditionProfile.createCountingProfile();

    public SIfNode(SchemeNode conditionNode, SchemeNode thenNode, SchemeNode elseNode) {
        this.conditionNode = Objects.requireNonNull(conditionNode);
        this.thenNode = Objects.requireNonNull(thenNode);
        this.elseNode = elseNode;
    }

    @Override
    public void setIsTail() {
        super.setIsTail();
        thenNode.setIsTail();
        if (elseNode != null) elseNode.setIsTail();
    }

    @Override
    public Object execute(VirtualFrame frame) {
        var conditionResult = conditionNode.execute(frame);

        // everything other than #F is truthy
        if (condition.profile(!conditionResult.equals(false))) {
            return thenNode.execute(frame);
        } else {
            if (elseNode != null) {
                return elseNode.execute(frame);
            } else {
                return Constants.UNSPECIFIED;
            }
        }
    }

    @Override
    public String toString() {
        return "(if %s %s %s)".formatted(conditionNode.toString(), thenNode.toString(), elseNode.toString());
    }
}
