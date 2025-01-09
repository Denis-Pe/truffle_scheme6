package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SIsInfiniteTest extends BuiltInTest {
    @Test
    void test() {
        assertFalse(eval("(infinite? 5.0)").asBoolean());
        assertTrue(eval("(infinite? +inf.0)").asBoolean());
        assertFalse(eval("(infinite? 3)").asBoolean());
    }
}