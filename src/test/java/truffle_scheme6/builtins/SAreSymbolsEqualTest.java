package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SAreSymbolsEqualTest extends BuiltInTest {
    @Test
    void test() {
        assertTrue(eval("(symbol=? 'yes 'yes 'yes 'yes 'yes)").asBoolean());
        assertFalse(eval("(symbol=? 'yes 'yes 'yes 'yes 'no)").asBoolean());
        assertTrue(eval("(symbol=? 'yes (string->symbol \"yes\"))").asBoolean());
        assertThrows(RuntimeException.class, () -> eval("(symbol=? 1 2 'number)"));
    }
}
