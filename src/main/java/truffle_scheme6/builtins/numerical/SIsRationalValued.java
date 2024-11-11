package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.nodes.STypesStrong;
import truffle_scheme6.nodes.functions.SReadArgSlotNode;
import truffle_scheme6.runtime.numbers.SComplexRational;
import truffle_scheme6.runtime.numbers.SFraction;

@BuiltinInfo(name = "rational-valued?")
@NodeChild(value = "arg", type = SReadArgSlotNode.class)
@TypeSystemReference(STypesStrong.class)
public abstract class SIsRationalValued extends SBuiltin {
    @Specialization
    public boolean doLong(long _l) {
        return true;
    }

    @Specialization
    public boolean doFloat(float _f) {
        return false;
    }

    @Specialization
    public boolean doDouble(double _d) {
        return false;
    }

    @Specialization
    public boolean doObject(Object arg) {
        return arg instanceof Integer
                || arg instanceof SFraction
                || (arg instanceof SComplexRational complex && complex.isRealValued());
    }
}
