package truffle_scheme6.builtins.pairs;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.runtime.SPair;

@BuiltinInfo(name = "pair?")
@NodeChild(value = "arg", type = SchemeNode.class)
public abstract class SIsPair extends SBuiltin {
    @Specialization
    public Object doObject(Object arg) {
        return arg instanceof SPair;
    }
}
