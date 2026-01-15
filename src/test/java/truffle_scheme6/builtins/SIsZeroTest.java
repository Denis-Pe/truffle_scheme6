package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SIsZeroTest extends BuiltInTest {
    @Test
    void test() {
        assertTrue(eval("(zero? 0)").asBoolean());
        assertTrue(eval("(zero? -0.0)").asBoolean());
        assertFalse(eval("(zero? 1)").asBoolean());
        assertFalse(eval("(zero? -inf.0)").asBoolean());
        assertFalse(eval("(zero? +nan.0)").asBoolean());
        assertFalse(eval("(zero? 3)").asBoolean());
        assertTrue(eval("(zero? 0+0i)").asBoolean());
    }
}