package truffle_scheme6.builtins.pairs;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.runtime.SNil;
import truffle_scheme6.runtime.SPair;

@BuiltinInfo(name = "length")
@NodeChild(value = "arg", type = SchemeNode.class)
public abstract class SLength extends SBuiltin {
    @Specialization
    public long doNil(SNil ignored) {
        return 0;
    }

    @Specialization
    public long doObject(SPair list) {
        return list.count();
    }
}
