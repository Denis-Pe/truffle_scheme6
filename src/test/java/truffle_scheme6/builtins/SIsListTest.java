package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SIsListTest extends BuiltInTest {
    @Test
    void test() {
        assertTrue(eval("(list? '(a b c))").asBoolean());
        assertTrue(eval("(list? '())").asBoolean());
        assertFalse(eval("(list? '(a . b))").asBoolean());
    }
}