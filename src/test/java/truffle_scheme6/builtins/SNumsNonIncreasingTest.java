package truffle_scheme6.builtins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SNumsNonIncreasingTest extends BuiltInTest {
    @Test
    void test() {
        assertTrue(eval("(>= #e9 9.0L0 (/ (/ 9)) #e8 8.0L0 (/ (/ 8)) #e7 7.0L0 (/ (/ 7)) #e6 6.0L0 (/ (/ 6)) #e5 5.0L0 (/ (/ 5)) #e4 4.0L0 (/ (/ 4)) #e3 3.0L0 (/ (/ 3)) #e2 2.0L0 (/ (/ 2)) #e1 1.0L0 (/ (/ 1)))").asBoolean());
    }
}