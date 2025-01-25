package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SIsRationalTest extends BuiltInTest {
    @Test
    void test() {
        assertTrue(eval("(rational? 6/10)").asBoolean());
        assertTrue(eval("(rational? 6/3)").asBoolean());
        assertTrue(eval("(rational? 2)").asBoolean());
        assertFalse(eval("(rational? +nan.0)").asBoolean());
        assertFalse(eval("(rational? -inf.0)").asBoolean());
    }

}