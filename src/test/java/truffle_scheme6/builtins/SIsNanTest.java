package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SIsNanTest extends BuiltInTest {
    @Test
    void test() {
        assertFalse(eval("(nan? 0)").asBoolean());
        assertFalse(eval("(nan? -1)").asBoolean());
        assertFalse(eval("(nan? 1)").asBoolean());
        assertFalse(eval("(nan? 1.0)").asBoolean());
        assertFalse(eval("(nan? -1.23456789)").asBoolean());
        assertTrue(eval("(nan? +nan.0)").asBoolean());
        assertFalse(eval("(nan? +inf.0)").asBoolean());
    }
}