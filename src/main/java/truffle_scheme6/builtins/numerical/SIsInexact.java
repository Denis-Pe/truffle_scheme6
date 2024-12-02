package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.nodes.STypesStrong;
import truffle_scheme6.runtime.numbers.SComplexDouble;
import truffle_scheme6.runtime.numbers.SComplexFloat;

@BuiltinInfo(name = "inexact?")
@NodeChild(value = "arg", type = SchemeNode.class)
@TypeSystemReference(STypesStrong.class)
public abstract class SIsInexact extends SBuiltin {
    @Specialization
    public boolean doObject(Object arg) {
        return arg instanceof Float || arg instanceof Double || arg instanceof SComplexDouble || arg instanceof SComplexFloat;
    }
}
