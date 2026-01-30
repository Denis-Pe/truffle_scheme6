package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;
import truffle_scheme6.runtime.SString;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SSymbolToStringTest extends BuiltInTest {
    public void testFor(String symbol) {
        assertTrue(eval("(string? (symbol->string '%s))".formatted(symbol)).asBoolean());
        assertEquals(symbol, eval("(symbol->string '%s)".formatted(symbol)).asString());
    }

    @Test
    public void test() {
        testFor("flying-fish");
        testFor("Martin");
        // todo rest of test cases once I have the other functions the standard's examples use
    }
}
