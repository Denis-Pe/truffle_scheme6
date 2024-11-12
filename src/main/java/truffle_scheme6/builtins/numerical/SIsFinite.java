package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.nodes.STypesStrong;
import truffle_scheme6.runtime.numbers.SFractionBigInt;
import truffle_scheme6.runtime.numbers.SFractionLong;

import java.math.BigDecimal;
import java.math.BigInteger;

@BuiltinInfo(name = "finite?")
@NodeChild(value = "arg", type = SchemeNode.class)
@TypeSystemReference(STypesStrong.class)
public abstract class SIsFinite extends SBuiltin {
    @Specialization
    public boolean doLong(long l) {
        return true;
    }

    @Specialization
    public boolean doBigInteger(BigInteger i) {
        return true;
    }

    @Specialization
    public boolean doFloat(float f) {
        return Float.isFinite(f);
    }

    @Specialization
    public boolean doDouble(double d) {
        return Double.isFinite(d);
    }

    @Specialization
    public boolean doBigDecimal(BigDecimal d) {
        return true;
    }

    @Specialization
    public boolean doLongFraction(SFractionLong fraction) {
        return true;
    }

    @Specialization
    public boolean doBigFraction(SFractionBigInt fraction) {
        return true;
    }
}
