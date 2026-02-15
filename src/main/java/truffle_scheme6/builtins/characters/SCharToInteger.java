package truffle_scheme6.builtins.characters;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.runtime.SChar;

@BuiltinInfo(name = "char->integer")
@NodeChild(value = "arg", type = SchemeNode.class)
public abstract class SCharToInteger extends SBuiltin {
    @Specialization
    public long doChar(SChar arg) {
        return arg.getValue();
    }
}
