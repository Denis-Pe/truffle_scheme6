package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SIsOddTest extends BuiltInTest {
    @Test
    void test() {
        assertTrue(eval("(odd? 1)").asBoolean());
        assertFalse(eval("(odd? 2)").asBoolean());
        assertTrue(eval("(odd? -33333333333333333333333333333333333333333333333333333333333333333)").asBoolean());
        assertFalse(eval("(odd? (* 1/2 12))").asBoolean());
    }
}