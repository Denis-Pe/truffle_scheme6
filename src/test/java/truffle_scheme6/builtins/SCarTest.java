package truffle_scheme6.builtins;

import org.graalvm.polyglot.PolyglotException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SCarTest extends BuiltInTest {
    @Test
    void test() {
        assertEquals("a", eval("(car '(a b c))").asString());
        var listA = eval("(car '((a) b c d))");
        assertTrue(listA.hasArrayElements());
        assertEquals("a", listA.getArrayElement(0).asString());
        assertEquals(1, eval("(car '(1 . 2))").asLong());
        assertThrows(PolyglotException.class, () -> eval("(car '())"));
    }
}