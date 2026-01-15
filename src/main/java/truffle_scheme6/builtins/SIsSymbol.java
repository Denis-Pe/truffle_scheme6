package truffle_scheme6.builtins;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.nodes.STypesStrong;
import truffle_scheme6.runtime.SSymbol;

@BuiltinInfo(name = "symbol?")
@NodeChild(value = "arg", type = SchemeNode.class)
@TypeSystemReference(STypesStrong.class)
public abstract class SIsSymbol extends SBuiltin {
    @Specialization
    public boolean isSymbol(Object arg) {
        return arg instanceof SSymbol;
    }
}
