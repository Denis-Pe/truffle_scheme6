package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SIsRationalValuedTest extends BuiltInTest {
    @Test
    void test() {
        assertFalse(eval("(rational-valued? +nan.0)").asBoolean());
        assertFalse(eval("(rational-valued? -inf.0)").asBoolean());
        assertTrue(eval("(rational-valued? 6/10)").asBoolean());
        assertTrue(eval("(rational-valued? 6/10+0.0i)").asBoolean());
        assertTrue(eval("(rational-valued? 6/10+0i)").asBoolean());
        assertTrue(eval("(rational-valued? 6/3)").asBoolean());
    }
}