package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SIsNullTest extends BuiltInTest {
    @Test
    void test() {
        assertTrue(eval("(null? '())").asBoolean());
        assertFalse(eval("(null? '(1 2))").asBoolean());
    }
}