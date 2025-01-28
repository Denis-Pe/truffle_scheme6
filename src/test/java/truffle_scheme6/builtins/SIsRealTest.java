package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SIsRealTest extends BuiltInTest {
    @Test
    void test() {
        assertTrue(eval("(real? 3)").asBoolean());
        assertFalse(eval("(real? -2.5+1.0i)").asBoolean());
        assertTrue(eval("(real? -2.5+0i)").asBoolean());
        assertTrue(eval("(real? -2.5)").asBoolean());
        assertTrue(eval("(real? #e1e10)").asBoolean());
        assertTrue(eval("(real? +nan.0)").asBoolean());
        assertTrue(eval("(real? -inf.0)").asBoolean());
    }
}