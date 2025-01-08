package truffle_scheme6.runtime.numbers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SComplexLongTest {
    @Test
    void isRealValued() {
        assertTrue(new SComplexLong(1, 0).isRealValued());
        assertTrue(new SComplexLong(0, 0).isRealValued());
        assertTrue(new SComplexLong(123, 0).isRealValued());
        assertFalse(new SComplexLong(0, 123).isRealValued());
        assertFalse(new SComplexLong(123, 123).isRealValued());
        assertTrue(new SComplexLong(Long.MIN_VALUE, 0).isRealValued());
    }

    @Test
    void add() {
        assertEquals(new SComplexLong(1, 1), new SComplexLong(1, 0).add(new SComplexLong(0, 1)));

        assertEquals(new SComplexLong(new SFractionLong(186, 216), new SFractionLong(732, 864)),
                new SComplexLong(new SFractionLong(7, 12), new SFractionLong(11, 36))
                        .add(new SComplexLong(new SFractionLong(5, 18), new SFractionLong(13, 24))));
    }

    @Test
    void subtract() {
        assertEquals(new SComplexLong(15, 0), new SComplexLong(30, 9999).subtract(new SComplexLong(15, 9999)));

        assertEquals(new SComplexLong(45, 10), new SComplexLong(30, 5).subtract(new SComplexLong(-15, -5)));
    }

    @Test
    void multiply() {
        assertEquals(new SComplexLong(0, 20),
                new SComplexLong(2, 4).multiply(new SComplexLong(4, 2)));

        assertEquals(new SComplexLong(27, 36),
                new SComplexLong(-3, 6).multiply(new SComplexLong(3, -6)));
    }

    @Test
    void divide() {
        assertEquals(new SComplexLong(new SFractionLong(4, 5), new SFractionLong(-3, 5)),
                new SComplexLong(2, 1).divide(new SComplexLong(1, 2)));

        assertEquals(new SComplexLong(-1, 0),
                new SComplexLong(6, -3).divide(new SComplexLong(-6, 3)));
    }

    @Test
    void negate() {
        assertEquals(new SComplexLong(1, 1), new SComplexLong(-1 ,-1).negate());
        
        assertEquals(new SComplexLong(-99, 0), new SComplexLong(99, 0).negate());
    }

    @Test
    void inverse() {
        assertEquals(new SComplexLong(new SFractionLong(1, 10), new SFractionLong(-1, 5)),
                new SComplexLong(2, 4).inverse());
        
        assertEquals(new SComplexLong(new SFractionLong(1, 15), new SFractionLong(2, 15)),
                new SComplexLong(3, -6).inverse());
    }
}