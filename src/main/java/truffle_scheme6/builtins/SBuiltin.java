package truffle_scheme6.builtins;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.SchemeNode;

@GenerateNodeFactory
public abstract class SBuiltin extends SchemeNode {
    @Override
    public final Object executeFrozen(VirtualFrame frame) {
        throw new UnsupportedOperationException("Can't freeze a builtin procedure node");
    }
}
