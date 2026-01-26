package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SMakeListTest extends BuiltInTest {
    @Test
    void test() {
        var a_7_c = eval("(list 'a (+ 3 4) 'c)");
        assertTrue(a_7_c.hasArrayElements());
        assertEquals(3L, a_7_c.getArraySize());
        assertEquals("a", a_7_c.getArrayElement(0).asString());
        assertEquals(7, a_7_c.getArrayElement(1).asLong());
        assertEquals("c", a_7_c.getArrayElement(2).asString());
        
        assertTrue(eval("(list)").isNull());
    }
}