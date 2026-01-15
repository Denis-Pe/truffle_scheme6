package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SNumsMinusTest extends BuiltInTest {
    @Test
    void test() {
        assertEquals(0, eval("(- 0)").asLong());
        assertEquals(-0.0, eval("(- 0.0)").asDouble());
        assertEquals(Double.NEGATIVE_INFINITY, eval("(- +inf.0)").asDouble());
        assertEquals(1, eval("(- -1)").asLong());
        assertEquals(-1, eval("(- 1)").asLong());
        assertEquals(2.778, eval("(- 3 1/5 #i.02 #e1/500)").asDouble());
    }
}