package truffle_scheme6.builtins;

import org.graalvm.polyglot.PolyglotException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SCdrTest extends BuiltInTest {
    @Test
    void test() {
        var listBCD = eval("(cdr '((a) b c d))");
        assertTrue(listBCD.hasArrayElements());
        assertEquals("b", listBCD.getArrayElement(0).asString());
        assertEquals("c", listBCD.getArrayElement(1).asString());
        assertEquals("d", listBCD.getArrayElement(2).asString());
        assertEquals(2L, eval("(cdr '(1 . 2))").asLong());
        assertThrows(PolyglotException.class, () -> eval("(cdr '())"));
    }
}