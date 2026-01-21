package truffle_scheme6.builtins.procedures;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.nodes.STypesStrong;
import truffle_scheme6.runtime.SLambda;

@BuiltinInfo(name = "procedure?")
@NodeChild(value = "arg", type = SchemeNode.class)
@TypeSystemReference(STypesStrong.class)
public abstract class SIsProcedure extends SBuiltin {
    @Specialization
    public boolean isProcedure(Object arg) {
        return arg instanceof SLambda;
    }
}
