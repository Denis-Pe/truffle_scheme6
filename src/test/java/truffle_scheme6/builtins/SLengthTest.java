package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SLengthTest extends BuiltInTest {
    @Test
    void test() {
        assertTrue(eval("(= 3 (length '(a b c)))").asBoolean());
        assertTrue(eval("(= 3 (length '(a (b) (c d e))))").asBoolean());
        assertTrue(eval("(zero? (length '()))").asBoolean());
    }
}