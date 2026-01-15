package truffle_scheme6.runtime.numbers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SComplexFloatTest {
    @Test
    void isRealValued() {
        assertTrue(new SComplexFloat(1, 0).isRealValued());
        assertTrue(new SComplexFloat(0, 0).isRealValued());
        assertTrue(new SComplexFloat(123, 0).isRealValued());
        assertFalse(new SComplexFloat(0, 123).isRealValued());
        assertFalse(new SComplexFloat(123, 123).isRealValued());
        assertTrue(new SComplexFloat(Float.MAX_VALUE, 0).isRealValued());
    }

    @Test
    void add() {
        assertEquals(new SComplexFloat(1, 1), new SComplexFloat(1, 0).add(new SComplexFloat(0, 1)));

        assertEquals(new SComplexFloat(127.999f, -58.5f),
                new SComplexFloat(127, 0.5f).add(new SComplexFloat(0.999f, -59.0f)));
    }

    @Test
    void subtract() {
        assertEquals(new SComplexFloat(15, 0),
                new SComplexFloat(30, 9999).subtract(new SComplexFloat(15, 9999)));

        assertEquals(new SComplexFloat(45, 10),
                new SComplexFloat(30, 5).subtract(new SComplexFloat(-15, -5)));
    }

    @Test
    void multiply() {
        assertEquals(new SComplexFloat(0, 20), new SComplexFloat(2, 4).multiply(new SComplexFloat(4, 2)));

        assertEquals(new SComplexFloat(27, 36), new SComplexFloat(-3, 6).multiply(new SComplexFloat(3, -6)));
    }

    @Test
    void divide() {
        assertEquals(new SComplexFloat(4.0f / 5.0f, -3.0f / 5.0f),
                new SComplexFloat(2, 1).divide(new SComplexFloat(1, 2)));

        assertEquals(new SComplexFloat(-1, 0), new SComplexFloat(6, -3).divide(new SComplexFloat(-6, 3)));
    }

    @Test
    void negate() {
        assertEquals(new SComplexFloat(1, 1), new SComplexFloat(-1, -1).negate());

        assertEquals(new SComplexFloat(-99, -0.0f), new SComplexFloat(99, 0).negate());
    }

    @Test
    void inverse() {
        assertEquals(new SComplexFloat(1.0f / 10.0f, -1.0f / 5.0f), new SComplexFloat(2, 4).inverse());

        assertEquals(new SComplexFloat(1.0f / 15.0f, 2.0f / 15.0f), new SComplexFloat(3.0f, -6.0f).inverse());
    }
}