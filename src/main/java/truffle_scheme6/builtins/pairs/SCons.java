package truffle_scheme6.builtins.pairs;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.runtime.SPair;

@BuiltinInfo(name = "cons")
@NodeChild(value = "obj1", type = SchemeNode.class)
@NodeChild(value = "obj2", type = SchemeNode.class)
public abstract class SCons extends SBuiltin {
    @Specialization
    public Object doObjects(Object obj1, Object obj2) {
        return new SPair(obj1, obj2);
    }
}
