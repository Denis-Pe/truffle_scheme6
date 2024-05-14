package truffle_scheme6.runtime;

import com.oracle.truffle.api.Assumption;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.utilities.CyclicAssumption;

public class SLambda {
    private RootCallTarget callTarget;
    private final CyclicAssumption callTargetStable;

    public SLambda(RootCallTarget callTarget) {
        this.callTarget = callTarget;
        this.callTargetStable = new CyclicAssumption(String.valueOf(this.hashCode()));
    }

    public RootCallTarget getCallTarget() {
        return callTarget;
    }

    public void setCallTarget(RootCallTarget callTarget) {
        this.callTarget = callTarget;
        callTargetStable.invalidate();
    }

    public Assumption getCallTargetStable() {
        return callTargetStable.getAssumption();
    }
}
