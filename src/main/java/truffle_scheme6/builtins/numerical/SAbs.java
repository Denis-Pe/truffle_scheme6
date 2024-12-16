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
import truffle_scheme6.runtime.numbers.SFractionBigInt;
import truffle_scheme6.runtime.numbers.SFractionLong;

@BuiltinInfo(name = "abs")
@NodeChild(value = "arg", type = SchemeNode.class)
@TypeSystemReference(STypesStrong.class)
public abstract class SAbs extends SBuiltin {
    @Specialization(rewriteOn = ArithmeticException.class)
    public long doLong(long l) {
        return Math.absExact(l);
    }

    @Specialization
    public SBigInt doBigInt(SBigInt bigInt) {
        return new SBigInt(bigInt.value().abs());
    }

    @Specialization(rewriteOn = ArithmeticException.class)
    public SFractionLong doLongFraction(SFractionLong fraction) {
        return fraction.absExact();
    }

    @Specialization
    public SFractionBigInt doBigFraction(SFractionBigInt fraction) {
        return fraction.abs();
    }

    @Specialization
    public SBigDec doBigDec(SBigDec bigDec) {
        return new SBigDec(bigDec.value().abs());
    }

    @Specialization
    public float doFloat(float f) {
        return Math.abs(f);
    }

    @Specialization
    public double doDouble(double d) {
        return Math.abs(d);
    }
}
