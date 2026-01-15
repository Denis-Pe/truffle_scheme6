package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SIsIntegerTest extends BuiltInTest {
    @Test
    void test() {
        assertTrue(eval("(integer? 0)").asBoolean());
        assertTrue(eval("(integer? 1)").asBoolean());
        assertTrue(eval("(integer? -1)").asBoolean());
        assertTrue(eval("(integer? 555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555)").asBoolean());
        assertTrue(eval("(integer? -555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555)").asBoolean());
        assertTrue(eval("(integer? (* 1/3 3))").asBoolean());
        assertTrue(eval("(integer? (* 1/3 -33333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333))").asBoolean());
    }
}