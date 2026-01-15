package truffle_scheme6.runtime.numbers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SComplexDoubleTest {
    @Test
    void isRealValued() {
        assertTrue(new SComplexDouble(1, 0).isRealValued());
        assertTrue(new SComplexDouble(0, 0).isRealValued());
        assertTrue(new SComplexDouble(123, 0).isRealValued());
        assertFalse(new SComplexDouble(0, 123).isRealValued());
        assertFalse(new SComplexDouble(123, 123).isRealValued());
        assertTrue(new SComplexDouble(Double.MAX_VALUE, 0).isRealValued());
    }

    @Test
    void add() {
        assertEquals(new SComplexDouble(1, 1), new SComplexDouble(1, 0).add(new SComplexDouble(0, 1)));

        assertEquals(new SComplexDouble(127.999, -58.5),
                new SComplexDouble(127, 0.5).add(new SComplexDouble(0.999, -59.0)));
    }

    @Test
    void subtract() {
        assertEquals(new SComplexDouble(15, 0),
                new SComplexDouble(30, 9999).subtract(new SComplexDouble(15, 9999)));

        assertEquals(new SComplexDouble(45, 10),
                new SComplexDouble(30, 5).subtract(new SComplexDouble(-15, -5)));
    }

    @Test
    void multiply() {
        assertEquals(new SComplexDouble(0, 20), new SComplexDouble(2, 4).multiply(new SComplexDouble(4, 2)));

        assertEquals(new SComplexDouble(27, 36), new SComplexDouble(-3, 6).multiply(new SComplexDouble(3, -6)));
    }

    @Test
    void divide() {
        assertEquals(new SComplexDouble(4.0 / 5.0, -3.0 / 5.0),
                new SComplexDouble(2, 1).divide(new SComplexDouble(1, 2)));

        assertEquals(new SComplexDouble(-1, 0), new SComplexDouble(6, -3).divide(new SComplexDouble(-6, 3)));
    }

    @Test
    void negate() {
        assertEquals(new SComplexDouble(1, 1), new SComplexDouble(-1, -1).negate());

        assertEquals(new SComplexDouble(-99, -0.0), new SComplexDouble(99, 0).negate());
    }

    @Test
    void inverse() {
        assertEquals(new SComplexDouble(1.0 / 10.0, -1.0 / 5.0), new SComplexDouble(2, 4).inverse());

        assertEquals(new SComplexDouble(1.0 / 15.0, 2.0 / 15.0), new SComplexDouble(3.0, -6.0).inverse());
    }
}