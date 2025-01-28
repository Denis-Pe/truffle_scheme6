package truffle_scheme6.builtins;

import org.graalvm.polyglot.PolyglotException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SNumsDivTest extends BuiltInTest {
    @Test
    void test() {
        assertEquals(3.0/20.0, eval("(/ 3 4 5)").asDouble());
        assertEquals(1.0/3.0, eval("(/ 3)").asDouble());
        assertEquals(Double.POSITIVE_INFINITY, eval("(/ 0.0)").asDouble());
        assertEquals(Double.POSITIVE_INFINITY, eval("(/ 1.0 0)").asDouble());
        assertEquals(Double.NEGATIVE_INFINITY, eval("(/ -1 0.0)").asDouble());
        assertEquals(0.0, eval("(/ +inf.0)").asDouble());
        assertThrows(PolyglotException.class, () -> eval("(/ 0 0)"));
        assertThrows(PolyglotException.class, () -> eval("(/ 3 0)"));
        assertEquals(0.0, eval("(/ 0 3.5)").asDouble());
        assertEquals(Double.NaN, eval("(/ 0 0.0)").asDouble());
        assertEquals(Double.NaN, eval("(/ 0.0 0)").asDouble());
        assertEquals(Double.NaN, eval("(/ 0.0 0.0)").asDouble());
    }
}