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

@BuiltinInfo(name = "negative?")
@NodeChild(value = "arg", type = SchemeNode.class)
@TypeSystemReference(STypesStrong.class)
public abstract class SIsNegative extends SBuiltin {
    @Specialization
    public boolean doLong(long l) {
        return l < 0L;
    }
    
    @Specialization
    public boolean doBigInteger(BigInteger i) {
        return i.compareTo(BigInteger.ZERO) < 0;
    }
    
    @Specialization
    public boolean doFloat(float f) {
        return f < 0.0f;
    }
    
    @Specialization
    public boolean doDouble(double d) {
        return d < 0.0d;
    }
    
    @Specialization
    public boolean doBigDecimal(BigDecimal d) {
        return d.compareTo(BigDecimal.ZERO) < 0;
    }
    
    @Specialization
    public boolean doLongFraction(SFractionLong fraction) {
        return fraction.numerator() < 0L;
    }
    
    @Specialization
    public boolean doBigFraction(SFractionBigInt fraction) {
        return fraction.numerator().compareTo(BigInteger.ZERO) < 0;
    }
}
