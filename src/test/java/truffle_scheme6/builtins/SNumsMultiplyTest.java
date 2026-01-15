package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SNumsMultiplyTest extends BuiltInTest {
    @Test
    void test() {
        assertEquals(1.0, eval("(* 9 (/ 9))").asDouble());
        assertEquals(0.2, eval("(* 8 1/4 1/2 1/5)").asDouble());
        assertEquals(-105.0, eval("(* -1 1.05 #e1e2)").asDouble());
    }
}