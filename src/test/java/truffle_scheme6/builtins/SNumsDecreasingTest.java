package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SNumsDecreasingTest extends BuiltInTest {
    @Test
    void test() {
        assertTrue(eval("(> 9 8 7 6 5 4 3 2 1 0 -1 -2 -3 -4 -5 -6 -7 -8 -9)").asBoolean());
        assertTrue(eval("(> 1/2 1/4)").asBoolean());
        assertTrue(eval("(> 1/4 -1.55)").asBoolean());
        assertTrue(eval("(> #e1000 #e100.11111111111 #e10.123 5/2 #e-123456789.987654321)").asBoolean());
    }
}