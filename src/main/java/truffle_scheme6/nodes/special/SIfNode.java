package truffle_scheme6.nodes.special;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.profiles.ConditionProfile;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.runtime.SIdentifier;
import truffle_scheme6.runtime.SNil;
import truffle_scheme6.runtime.SPair;

public class SIfNode extends SchemeNode {
    @Child private SchemeNode conditionNode;
    @Child private SchemeNode thenNode;
    @Child private SchemeNode elseNode; // optional
    private final ConditionProfile condition = ConditionProfile.createCountingProfile();

    public SIfNode(SchemeNode conditionNode, SchemeNode thenNode, SchemeNode elseNode) {
        this.conditionNode = conditionNode;
        this.thenNode = thenNode;
        this.elseNode = elseNode;
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
                return SNil.SINGLETON;
            }
        }
    }

    @Override
    public Object executeFrozen(VirtualFrame frame) {
        if (elseNode != null) {
            return SPair.from(
                    new SIdentifier("if"),
                    conditionNode.executeFrozen(frame),
                    thenNode.executeFrozen(frame),
                    elseNode.executeFrozen(frame),
                    SNil.SINGLETON
            );
        } else {
            return SPair.from(
                    new SIdentifier("if"),
                    conditionNode.executeFrozen(frame),
                    thenNode.executeFrozen(frame),
                    SNil.SINGLETON
            );
        }
    }

    public SchemeNode getConditionNode() {
        return conditionNode;
    }

    public SchemeNode getThenNode() {
        return thenNode;
    }

    public SchemeNode getElseNode() {
        return elseNode;
    }

    @Override
    public String toString() {
        return "(if %s %s %s)".formatted(conditionNode.toString(), thenNode.toString(), elseNode.toString());
    }
}
