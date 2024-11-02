package truffle_scheme6.builtins;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import truffle_scheme6.nodes.functions.SReadArgSlotNode;
import truffle_scheme6.runtime.numbers.*;

@NodeInfo(shortName = "number?")
@NodeChild(value = "arg", type = SReadArgSlotNode.class)
public abstract class SIsNumber extends SBuiltin {
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
        return arg instanceof SBigInt
                || arg instanceof SFixnum
                || arg instanceof SFractionBigInt
                || arg instanceof SFractionLong
                || arg instanceof SComplexBigDec
                || arg instanceof SComplexDouble
                || arg instanceof SComplexFloat
                || arg instanceof SComplexRational;
    }
}