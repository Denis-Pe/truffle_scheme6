package truffle_scheme6.builtins.pairs;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.runtime.SNil;
import truffle_scheme6.runtime.SPair;

@BuiltinInfo(name = "list?")
@NodeChild(value = "arg", type = SchemeNode.class)
public abstract class SIsList extends SBuiltin {
    @Specialization
    public boolean doPair(Object arg) {
        return arg == SNil.SINGLETON || arg instanceof SPair pair && pair.isProperList();
    }
}
