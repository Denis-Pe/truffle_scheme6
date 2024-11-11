package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.nodes.STypesStrong;
import truffle_scheme6.nodes.functions.SReadArgSlotNode;
import truffle_scheme6.runtime.numbers.SComplexDouble;
import truffle_scheme6.runtime.numbers.SComplexFloat;

@BuiltinInfo(name = "inexact?")
@NodeChild(value = "arg", type = SReadArgSlotNode.class)
@TypeSystemReference(STypesStrong.class)
public abstract class SIsInexact extends SBuiltin {
    @Specialization
    public boolean doLong(long _l) {
        return false;
    }

    @Specialization
    public boolean doFloat(float f) {
        return true;
    }

    @Specialization
    public boolean doDouble(double d) {
        return true;
    }

    @Specialization
    public boolean doObject(Object arg) {
        return arg instanceof SComplexDouble || arg instanceof SComplexFloat;
    }
}
