package truffle_scheme6.builtins;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.nodes.STypesStrong;
import truffle_scheme6.nodes.functions.SReadArgSlotNode;
import truffle_scheme6.runtime.numbers.*;

import java.math.BigDecimal;

@BuiltinInfo(name = "exact?")
@NodeChild(value = "arg", type = SReadArgSlotNode.class)
@TypeSystemReference(STypesStrong.class)
public abstract class SIsExact extends SBuiltin {
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
        return arg instanceof SBigInt
                || arg instanceof SFixnum
                || arg instanceof BigDecimal
                || arg instanceof SFractionBigInt
                || arg instanceof SFractionLong
                || arg instanceof SComplexBigDec
                || arg instanceof SComplexRational;
    }
}
