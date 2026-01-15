package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SIsComplexTest extends BuiltInTest {
    @Test
    void test() {
        assertTrue(eval("(complex? 3+4i)").asBoolean());
        assertTrue(eval("(complex? 3)").asBoolean());
        assertTrue(eval("(complex? +nan.0)").asBoolean());
        assertTrue(eval("(complex? +inf.0)").asBoolean());
        assertFalse(eval("(complex? 'rapp-snitch-knishes)").asBoolean());
        assertTrue(eval("(complex? 1.21341353535135315)").asBoolean());
    }
}