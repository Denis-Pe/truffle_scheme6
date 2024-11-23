package truffle_scheme6.builtins.pairs;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.runtime.SNil;
import truffle_scheme6.runtime.SPair;

@BuiltinInfo(name = "list", lastVarArgs = true)
@NodeChild(value = "args", type = SchemeNode.class)
public abstract class SList extends SBuiltin {
    @Specialization
    public Object doObjectArr(Object[] args) {
        if (args.length == 0) {
            return SNil.SINGLETON;
        } else {
            return SPair.list(args);
        }
    }
}
