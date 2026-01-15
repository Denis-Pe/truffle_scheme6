package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SAbsTest extends BuiltInTest {
    @Test
    void test() {
        assertEquals(1.222, eval("(abs 1.222)").asDouble());
        assertEquals(123, eval("(abs -123)").asLong());
    }
}