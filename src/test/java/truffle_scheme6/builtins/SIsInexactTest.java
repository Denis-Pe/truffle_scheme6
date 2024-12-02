package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SIsInexactTest extends BuiltInTest {
    @Test
    void test() {
        assertTrue(eval("(inexact? 1.222)").asBoolean());
        assertFalse(eval("(inexact? #e1.222)").asBoolean());
        assertTrue(eval("(inexact? +inf.0)").asBoolean());
        assertFalse(eval("(inexact? #e1.234567890101L1234)").asBoolean());
        assertFalse(eval("(inexact? 123)").asBoolean());
        assertFalse(eval("(inexact? 0)").asBoolean());
        assertFalse(eval("(inexact? 0/123)").asBoolean());
        assertFalse(eval("(inexact? 12/315)").asBoolean());
        assertTrue(eval("(inexact? +nan.0)").asBoolean());
    }
}