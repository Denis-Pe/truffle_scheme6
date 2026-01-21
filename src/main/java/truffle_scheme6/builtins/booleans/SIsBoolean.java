package truffle_scheme6.builtins.booleans;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.nodes.STypesStrong;

@BuiltinInfo(name = "boolean?")
@NodeChild(value = "arg", type = SchemeNode.class)
@TypeSystemReference(STypesStrong.class)
public abstract class SIsBoolean extends SBuiltin {
    @Specialization
    public boolean isSymbol(Object arg) {
        return arg instanceof Boolean;
    }
}
