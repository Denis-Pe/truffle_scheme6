package truffle_scheme6.runtime;

import com.oracle.truffle.api.CallTarget;

public class SLambda {
    public final CallTarget callTarget;

    public SLambda(CallTarget callTarget) {
        this.callTarget = callTarget;
    }
}
