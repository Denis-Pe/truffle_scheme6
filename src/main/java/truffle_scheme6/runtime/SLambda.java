package truffle_scheme6.runtime;

import com.oracle.truffle.api.Assumption;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.ReportPolymorphism;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.ArityException;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.oracle.truffle.api.nodes.DirectCallNode;
import com.oracle.truffle.api.nodes.IndirectCallNode;
import com.oracle.truffle.api.utilities.CyclicAssumption;
import truffle_scheme6.nodes.functions.RecursiveTailCallException;

@ExportLibrary(InteropLibrary.class)
public class SLambda implements TruffleObject {
    private RootCallTarget callTarget;
    private final CyclicAssumption callTargetStable;

    private final long minArgs;
    private final boolean isVariadic;

    public SLambda(RootCallTarget callTarget, long minArgs, boolean isVariadic) {
        this.callTarget = callTarget;
        this.minArgs = minArgs;
        this.isVariadic = isVariadic;
        this.callTargetStable = new CyclicAssumption(String.valueOf(this.hashCode()));
    }

    public RootCallTarget getCallTarget() {
        return callTarget;
    }

    public void setCallTarget(RootCallTarget callTarget) {
        this.callTarget = callTarget;
        callTargetStable.invalidate();
    }

    /**
     * For the arity of a given call, whether the lambda can accept it
     */
    private boolean isArityValid(long numberArgs) {
        if (isVariadic) {
            return numberArgs >= minArgs;
        } else {
            return numberArgs == minArgs;
        }
    }

    @ExportMessage
    boolean isExecutable() {
        return true;
    }

    @ReportPolymorphism
    @ExportMessage
    abstract static class Execute {
        @Specialization(
                limit = "2",
                guards = "function.getCallTarget() == cachedTarget",
                assumptions = "callTargetStable")
        protected static Object directDispatch(
                SLambda function, Object[] arguments,
                @Cached("create(function.getCallTarget())") DirectCallNode callNode,
                @Cached("function.getCallTarget()") RootCallTarget cachedTarget,
                @Cached("function.getCallTargetStable()") Assumption callTargetStable) throws ArityException {
            while (true) {
                try {
                    if (!function.isArityValid(arguments.length)) {
                        throw ArityException.create((int) function.minArgs, function.isVariadic ? -1 : (int) function.minArgs, arguments.length);
                    }

                    return callNode.call(arguments);
                } catch (RecursiveTailCallException e) {
                    arguments = e.arguments;
                }
            }
        }

        @Specialization(replaces = "directDispatch")
        protected static Object indirectDispatch(
                SLambda function, Object[] arguments,
                @Cached IndirectCallNode callNode) throws ArityException {
            if (!function.isArityValid(arguments.length)) {
                throw ArityException.create((int) function.minArgs, function.isVariadic ? -1 : (int) function.minArgs, arguments.length);
            }

            return callNode.call(function.getCallTarget(), arguments);
        }
    }

    public Assumption getCallTargetStable() {
        return callTargetStable.getAssumption();
    }
}
