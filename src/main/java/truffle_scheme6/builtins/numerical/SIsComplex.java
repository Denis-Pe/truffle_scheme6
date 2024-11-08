package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.nodes.STypesStrong;
import truffle_scheme6.nodes.functions.SReadArgSlotNode;
import truffle_scheme6.runtime.numbers.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@BuiltinInfo(name = "complex?")
@NodeChild(value = "arg", type = SReadArgSlotNode.class)
@TypeSystemReference(STypesStrong.class)
public abstract class SIsComplex extends SBuiltin {
    @Specialization
    public boolean doLong(long _l) {
        return true;
    }

    @Specialization
    public boolean doFloat(float _f) {
        return true;
    }

    @Specialization
    public boolean doDouble(double _d) {
        return true;
    }

    @Specialization
    public boolean doObject(Object arg) {
        return arg instanceof BigInteger
                || arg instanceof BigDecimal
                || arg instanceof SFraction
                || arg instanceof SComplex;
    }
}
