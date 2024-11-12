package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.nodes.STypesStrong;
import truffle_scheme6.runtime.numbers.SFraction;

import java.math.BigDecimal;
import java.math.BigInteger;

@BuiltinInfo(name = "nan?")
@NodeChild(value = "arg", type = SchemeNode.class)
@TypeSystemReference(STypesStrong.class)
public abstract class SIsNan extends SBuiltin {
    @Specialization
    public boolean doLong(long l) {
        return false;
    }

    @Specialization
    public boolean doBigInteger(BigInteger i) {
        return false;
    }

    @Specialization
    public boolean doFloat(float f) {
        return Float.isNaN(f);
    }

    @Specialization
    public boolean doDouble(double d) {
        return Double.isNaN(d);
    }

    @Specialization
    public boolean doBigDecimal(BigDecimal d) {
        return false;
    }

    @Specialization
    public boolean doFraction(SFraction fraction) {
        return false;
    }
}
