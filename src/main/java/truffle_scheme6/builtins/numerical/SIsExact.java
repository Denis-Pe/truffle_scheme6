package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.nodes.STypesStrong;
import truffle_scheme6.runtime.numbers.*;

@BuiltinInfo(name = "exact?")
@NodeChild(value = "arg", type = SchemeNode.class)
@TypeSystemReference(STypesStrong.class)
public abstract class SIsExact extends SBuiltin {
    @Specialization
    public boolean doObject(Object arg) {
        return arg instanceof Long
                || arg instanceof SBigInt
                || arg instanceof SBigDec
                || arg instanceof SFraction
                || arg instanceof SComplexBigDec
                || arg instanceof SComplexRational;
    }
}
