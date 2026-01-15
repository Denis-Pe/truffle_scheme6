package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SNumsIncreasingTest extends BuiltInTest {
    @Test
    void test() {
        assertTrue(eval("(< -9 -8 -7 -6 -5 -4 -3 -2 -1 0 1 2 3 4 5 6 7 8 9)").asBoolean());
        assertTrue(eval("(< 1/1000 #e1/100 #i1/10 1.0 #e10.0 100.0s0 1.0d3)").asBoolean());
        assertTrue(eval("(< -inf.0 0 1.0 #e10.0 +inf.0)").asBoolean());
    }
}