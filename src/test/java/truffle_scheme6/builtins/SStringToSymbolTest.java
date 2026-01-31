package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SStringToSymbolTest extends BuiltInTest {
    @Test
    public void test() {
        assertFalse(eval("(string? (string->symbol \"yes\"))").asBoolean());
        assertTrue(eval("(symbol? (string->symbol \"yes\"))").asBoolean());
        assertEquals("yes", eval("(string->symbol \"yes\")").asString());
    }
}
