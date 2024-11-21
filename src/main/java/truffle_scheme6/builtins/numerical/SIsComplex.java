package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.nodes.STypesStrong;
import truffle_scheme6.runtime.numbers.SBigDec;
import truffle_scheme6.runtime.numbers.SBigInt;
import truffle_scheme6.runtime.numbers.SComplex;
import truffle_scheme6.runtime.numbers.SFraction;

@BuiltinInfo(name = "complex?")
@NodeChild(value = "arg", type = SchemeNode.class)
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
        return arg instanceof SBigInt
                || arg instanceof SBigDec
                || arg instanceof SFraction
                || arg instanceof SComplex;
    }
}
