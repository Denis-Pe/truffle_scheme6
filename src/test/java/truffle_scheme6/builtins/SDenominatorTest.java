package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SDenominatorTest extends BuiltInTest {
    @Test
    void test() {
        assertEquals(1L, eval("(denominator 0)").asLong());
        assertEquals(2L, eval("(denominator (/ 6 4))").asLong());
    }
}