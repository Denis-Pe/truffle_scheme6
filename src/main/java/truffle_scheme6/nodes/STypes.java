package truffle_scheme6.nodes;

import com.oracle.truffle.api.dsl.ImplicitCast;
import com.oracle.truffle.api.dsl.TypeCheck;
import com.oracle.truffle.api.dsl.TypeSystem;
import truffle_scheme6.runtime.SNil;

// Bytes in Scheme are always unsigned
@TypeSystem({boolean.class, byte.class, int.class, long.class, float.class, double.class})
public abstract class STypes {
    @TypeCheck(SNil.class)
    public static boolean isSNull(Object value) {
        return value == SNil.SINGLETON;
    }

    @ImplicitCast
    public static int castInt(byte value) {
        return 0xFF & value;
    }
}
