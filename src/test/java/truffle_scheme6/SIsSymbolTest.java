package truffle_scheme6;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SIsSymbolTest extends BuiltInTest {
    @Test
    void test() {
        assertTrue(eval("(symbol? 'yes)").asBoolean());
        assertFalse(eval("(symbol? 2)").asBoolean());
        assertFalse(eval("(symbol? '(1 2 3 4 5))").asBoolean());
    }
}