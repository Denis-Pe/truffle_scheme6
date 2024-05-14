package truffle_scheme6.nodes.functions;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.DirectCallNode;
import com.oracle.truffle.api.nodes.IndirectCallNode;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.Assumption;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.runtime.SLambda;

public abstract class SFuncDispatchNode extends Node {
    public abstract Object execute(Object function, Object[] arguments);

    @Specialization(
            limit = "2",
            guards = "function.getCallTarget() == cachedTarget",
            assumptions = "callTargetStable")
    protected static Object directDispatch(
            SLambda function, Object[] arguments,
            @Cached("create(function.getCallTarget())") DirectCallNode callNode,
            @Cached("function.getCallTarget()") RootCallTarget cachedTarget,
            @Cached("function.getCallTargetStable()") Assumption callTargetStable) {
        return callNode.call(arguments);
    }

    @Specialization(replaces = "directDispatch")
    protected static Object indirectDispatch(
            SLambda function, Object[] arguments,
            @Cached IndirectCallNode callNode) {
        return callNode.call(function.getCallTarget(), arguments);
    }

    @Fallback
    protected static Object notAFunction(
            Object f, Object[] _args) {
        throw new RuntimeException("Tried to execute " + f + " as a function");
    }
}
