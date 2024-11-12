package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.nodes.STypesStrong;
import truffle_scheme6.runtime.numbers.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@BuiltinInfo(name = "zero?")
@NodeChild(value = "arg", type = SchemeNode.class)
@TypeSystemReference(STypesStrong.class)
public abstract class SIsZero extends SBuiltin {
    @Specialization
    public boolean doLong(long l) {
        return l == 0;
    }
    
    @Specialization
    public boolean doBigInteger(BigInteger i) {
        return i.equals(BigInteger.ZERO);
    }
    
    @Specialization
    public boolean doFloat(float f) {
        return f == 0.0f;
    }
    
    @Specialization
    public boolean doDouble(double d) {
        return d == 0.0;
    }
    
    @Specialization
    public boolean doBigDecimal(BigDecimal d) {
        return d.compareTo(BigDecimal.ZERO) == 0;
    }
    
    @Specialization
    public boolean doFraction(SFraction fraction) {
        return fraction.isZero();
    }
    
    @Specialization
    public boolean doComplexRational(SComplexRational complex) {
        return complex.real().isZero() && complex.imag().isZero();
    }
    
    @Specialization
    public boolean doComplexFloat(SComplexFloat complex) {
        return complex.real() == 0.0f && complex.imag() == 0.0f;
    }
    
    @Specialization
    public boolean doComplexDouble(SComplexDouble complex) {
        return complex.real() == 0.0d && complex.imag() == 0.0d;
    }
    
    @Specialization
    public boolean doComplexBigDec(SComplexBigDec complex) {
        return complex.real().compareTo(BigDecimal.ZERO) == 0 && complex.imag().compareTo(BigDecimal.ZERO) == 0;
    }
}
