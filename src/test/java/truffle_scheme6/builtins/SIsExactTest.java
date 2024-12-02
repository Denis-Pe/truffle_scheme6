package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SIsExactTest extends BuiltInTest {
    @Test
    void test() {
        assertTrue(eval("(exact? 0)").asBoolean());
        assertTrue(eval("(exact? 1)").asBoolean());
        assertTrue(eval("(exact? 1/2)").asBoolean());
        assertFalse(eval("(exact? #i1/2)").asBoolean());
        assertFalse(eval("(exact? 1.2)").asBoolean());
        assertFalse(eval("(exact? 1.2s10)").asBoolean());
        assertTrue(eval("(exact? #e1.2L10)").asBoolean());
        assertFalse(eval("(exact? +inf.0)").asBoolean());
        assertTrue(eval("(exact? 2)").asBoolean());
    }
}