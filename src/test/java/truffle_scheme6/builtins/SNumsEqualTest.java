package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SNumsEqualTest extends BuiltInTest {
    @Test
    void test() {
        assertTrue(eval("(= 1 1.0 (* 2 #e1/2) #e1.0 #e1)").asBoolean());
        assertTrue(eval("(= 1+i 1.0+1.i)").asBoolean());
        assertTrue(eval("(= -44 -44.0 #e-44 #e-44.0000000000 (* -44 1.0s0))").asBoolean());
    }
}