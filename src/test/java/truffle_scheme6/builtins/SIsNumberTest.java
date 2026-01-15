package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SIsNumberTest extends BuiltInTest {
    @Test
    void test() {
        assertTrue(eval("(number? 0)").asBoolean());
        assertTrue(eval("(number? 11111111111111111111111111111111111111111111111111111111111)").asBoolean());
        assertTrue(eval("(number? 1.0f0)").asBoolean());
        assertTrue(eval("(number? 2.0d0)").asBoolean());
        assertTrue(eval("(number? 1/25)").asBoolean());
        assertTrue(eval("(number? -1/255555555555555555555555555555555555555555555555555555555555)").asBoolean());
        assertTrue(eval("(number? #e1.2555)").asBoolean());
        assertTrue(eval("(number? 1/5+i)").asBoolean());
        assertFalse(eval("(number? 'thisisaninvitation)").asBoolean());
        assertFalse(eval("(number? #\\r)").asBoolean());
        assertFalse(eval("(number? \"the same lie a thousand times\")").asBoolean());
        assertTrue(eval("(number? #e1.5)").asBoolean());
    }
}