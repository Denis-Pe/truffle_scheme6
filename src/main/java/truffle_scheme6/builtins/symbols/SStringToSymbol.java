package truffle_scheme6.builtins.symbols;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.runtime.SString;
import truffle_scheme6.runtime.SSymbol;

@BuiltinInfo(name = "string->symbol")
@NodeChild(value = "arg", type = SchemeNode.class)
public abstract class SStringToSymbol extends SBuiltin {
    @Specialization
    public SSymbol doString(SString arg) {
        return SSymbol.get(arg.getValue());
    }
}
