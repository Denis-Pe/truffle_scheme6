package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SConsTest extends BuiltInTest {
    @Test
    void test() {
        var consA_Nil = eval("(cons 'a '())");
        assertTrue(consA_Nil.hasArrayElements());
        assertEquals(1, consA_Nil.getArraySize());
        assertEquals("a", consA_Nil.getArrayElement(0).asString());
        
        var consA_BCD = eval("(cons '(a) '(b c d))");
        assertTrue(consA_BCD.hasArrayElements());
        assertEquals(4, consA_BCD.getArraySize());
        assertTrue(consA_BCD.getArrayElement(0).hasArrayElements());
        
        var consA_BC = eval("(cons \"a\" '(b c))");
        assertTrue(consA_BC.hasArrayElements());
        assertEquals(3, consA_BC.getArraySize());
    }
}