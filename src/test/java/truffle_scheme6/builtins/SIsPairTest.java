package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SIsPairTest extends BuiltInTest {
    @Test
    void test() {
        assertTrue(eval("(pair? '(a . b))").asBoolean());
        assertTrue(eval("(pair? '(a b c))").asBoolean());
        assertFalse(eval("(pair? '())").asBoolean());
        assertFalse(eval("(pair? '#(a b))").asBoolean());
    }
}