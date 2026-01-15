package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SNumsAddTest extends BuiltInTest {
    @Test
    void test() {
        assertEquals(1, eval("(+ 1)").asLong());
        assertEquals(-2L, eval("(+ -4 2)").asLong());
        assertEquals(2.222, eval("(+ #e2.0 #e0.2 #e0.02 #e0.002)").asDouble());
        assertEquals(-2.222, eval("(+ -2.0 -0.2 -0.02 -0.002)").asDouble());
    }
}