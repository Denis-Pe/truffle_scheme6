package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.nodes.functions.SReadArgSlotNode;
import truffle_scheme6.nodes.functions.SReadVarArgsNode;

@BuiltinInfo(name = "=", lastVarArgs = true)
@NodeChild(value = "args", type = SReadVarArgsNode.class)
public abstract class SNumsEqual extends SBuiltin {
    @Specialization
    public Object doObjectArr(Object[] args) {
        return null;
    }
}
