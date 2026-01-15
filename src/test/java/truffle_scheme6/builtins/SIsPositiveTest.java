package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SIsPositiveTest extends BuiltInTest {
    @Test
    void test() {
        assertFalse(eval("(positive? -1)").asBoolean());
        assertFalse(eval("(positive? -1111111111111111111111111111111111111111111111111111111111111111111111111111111111111)").asBoolean());
        assertFalse(eval("(positive? (* 1/3 -1111111111111111111111111111111111111111111111111111111111))").asBoolean());
        assertFalse(eval("(positive? 0)").asBoolean());
        assertTrue(eval("(positive? 1)").asBoolean());
        assertTrue(eval("(positive? 9999999999999999999999999999999999999999999999999999999999)").asBoolean());
        assertFalse(eval("(positive? -0.1)").asBoolean());
        assertFalse(eval("(positive? -1f-10)").asBoolean());
        assertTrue(eval("(positive? 1/99999999999999999999999999999999999999999999999999999999)").asBoolean());
    }
}