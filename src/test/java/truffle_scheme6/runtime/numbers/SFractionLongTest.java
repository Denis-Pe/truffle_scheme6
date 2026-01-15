package truffle_scheme6.runtime.numbers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SFractionLongTest {

    @Test
    void gcd() {
        assertEquals(3, new SFractionLong(3, 9).gcd());
        assertEquals(9, new SFractionLong(987654321, 123456789).gcd());
        assertEquals(1, new SFractionLong(83484985, 5463452).gcd());
    }
}