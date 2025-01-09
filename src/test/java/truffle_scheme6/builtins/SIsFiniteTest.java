package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SIsFiniteTest extends BuiltInTest {
    @Test
    void test() {
        assertFalse(eval("(finite? +inf.0)").asBoolean());
        assertTrue(eval("(finite? 5)").asBoolean());
        assertTrue(eval("(finite? 5.0)").asBoolean());
    }
}