package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.nodes.STypesStrong;

import java.math.BigInteger;

@BuiltinInfo(name = "even?")
@NodeChild(value = "arg", type = SchemeNode.class)
@TypeSystemReference(STypesStrong.class)
public abstract class SIsEven extends SBuiltin {
    @Specialization
    public boolean doLong(long l) {
        return l % 2 == 0;
    }
    
    @Specialization
    public boolean doBigInteger(BigInteger i) {
        return i.remainder(BigInteger.TWO).compareTo(BigInteger.ZERO) == 0;
    }
}
