package truffle_scheme6.nodes.functions;

import com.oracle.truffle.api.nodes.ControlFlowException;

public class RecursiveTailCallException extends ControlFlowException {
    public final Object[] arguments;

    public RecursiveTailCallException(Object[] arguments) {
        this.arguments = arguments;
    }
}
