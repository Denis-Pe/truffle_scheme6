package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SIsNegativeTest extends BuiltInTest {
    @Test
    void test() {
        assertTrue(eval("(negative? -1)").asBoolean());
        assertTrue(eval("(negative? -1111111111111111111111111111111111111111111111111111111111111111111111111111111111111)").asBoolean());
        assertTrue(eval("(negative? (* 1/3 -1111111111111111111111111111111111111111111111111111111111))").asBoolean());
        assertFalse(eval("(negative? 0)").asBoolean());
        assertFalse(eval("(negative? 1)").asBoolean());
        assertFalse(eval("(negative? 9999999999999999999999999999999999999999999999999999999999)").asBoolean());
        assertTrue(eval("(negative? -0.1)").asBoolean());
        assertTrue(eval("(negative? -1f-10)").asBoolean());
    }
}