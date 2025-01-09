package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SIsEvenTest extends BuiltInTest {
    @Test
    void test() {
        assertTrue(eval("(even? 2)").asBoolean());
        assertTrue(eval("(even? 22)").asBoolean());
        assertTrue(eval("(even? 22222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222)").asBoolean());
        assertFalse(eval("(even? 1)").asBoolean());
        assertFalse(eval("(even? 9)").asBoolean());
        assertFalse(eval("(even? 1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111)").asBoolean());
        assertFalse(eval("(even? (* 3/2 2))").asBoolean());
        assertTrue(eval("(even? (* 5/2 1/2 8))").asBoolean());
        assertTrue(eval("(even? 1248)").asBoolean());
    }
}