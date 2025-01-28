package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class SNumsNonDecreasingTest extends BuiltInTest {
    @Test
    void test() {
        assertTrue(eval("(<= #e1 1.0 (/ (/ 1)) #e2 2.0 (/ (/ 2)) #e3 3.0 (/ (/ 3)) #e4 4.0 (/ (/ 4)))").asBoolean());
        assertTrue(eval("(<= -1 #e0d100 0 1.0s0)").asBoolean());
    }
}