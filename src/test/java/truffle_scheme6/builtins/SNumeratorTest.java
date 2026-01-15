package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SNumeratorTest extends BuiltInTest {
    @Test
    void test() {
        assertEquals(3L, eval("(numerator (/ 6 4))").asLong());
        assertEquals(3L, eval("(numerator (/ 12 4))").asLong());
        assertEquals(-5L, eval("(numerator (- (/ 10 4)))").asLong());
        assertEquals(1L, eval("(numerator 1)").asLong());
        assertEquals(1L, eval("(numerator (/ 55 55))").asLong());
    }
}