package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IntegerCharConversionsTest extends BuiltInTest {
    @Test
    public void doTest() {
        assertEquals("#\\space", eval("(integer->char 32)").asString());
        assertEquals(5000,  eval("(char->integer (integer->char 5000))").asInt());
        assertThrows(Exception.class,  () -> eval("(integer->char #\\xD800)"));
    }
}
