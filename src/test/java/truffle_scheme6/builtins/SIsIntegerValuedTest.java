package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SIsIntegerValuedTest extends BuiltInTest {
    @Test
    void test() {
        assertTrue(eval("(integer-valued? 0)").asBoolean());
        assertTrue(eval("(integer-valued? -1)").asBoolean());
        assertTrue(eval("(integer-valued? 1)").asBoolean());
        assertTrue(eval("(integer-valued? (* 3 1/3))").asBoolean());
        assertTrue(eval("(integer-valued? (* 3 (* 2 1/6)))").asBoolean());
        assertTrue(eval("(integer-valued? 1+0i)").asBoolean());
    }
}