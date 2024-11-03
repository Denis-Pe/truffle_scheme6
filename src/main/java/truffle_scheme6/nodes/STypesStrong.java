package truffle_scheme6.nodes;

import com.oracle.truffle.api.dsl.TypeCheck;
import com.oracle.truffle.api.dsl.TypeSystem;
import truffle_scheme6.runtime.SNil;

/**
 * Used for operations where implicit casting is not desired,
 * e.g. type checks where a float or double would otherwise come
 * from the implicit cast of another value
 */
@TypeSystem({boolean.class, byte.class, float.class, double.class})
public abstract class STypesStrong {
    @TypeCheck(SNil.class)
    public static boolean isSNull(Object value) {
        return value == SNil.SINGLETON;
    }
}
