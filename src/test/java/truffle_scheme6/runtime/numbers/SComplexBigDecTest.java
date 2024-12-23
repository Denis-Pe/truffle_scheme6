package truffle_scheme6.runtime.numbers;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SComplexBigDecTest {

    @Test
    void isRealValued() {
        assertTrue(new SComplexBigDec(BigDecimal.valueOf(1), BigDecimal.valueOf(0)).isRealValued());
        assertTrue(new SComplexBigDec(BigDecimal.valueOf(0), BigDecimal.valueOf(0)).isRealValued());
        assertTrue(new SComplexBigDec(BigDecimal.valueOf(123.214), BigDecimal.valueOf(0)).isRealValued());
        assertFalse(new SComplexBigDec(BigDecimal.valueOf(0), BigDecimal.valueOf(1)).isRealValued());
        assertFalse(new SComplexBigDec(BigDecimal.valueOf(123), BigDecimal.valueOf(123)).isRealValued());
        assertTrue(new SComplexBigDec(new BigDecimal("123.3e100"), BigDecimal.valueOf(0)).isRealValued());
    }

    @Test
    void add() {
        assertEquals(new SComplexBigDec(BigDecimal.valueOf(1), BigDecimal.valueOf(1)),
                new SComplexBigDec(BigDecimal.valueOf(1), BigDecimal.valueOf(0))
                        .add(new SComplexBigDec(BigDecimal.valueOf(0), BigDecimal.valueOf(1))));

        assertEquals(new SComplexBigDec(new BigDecimal("-8888"), new BigDecimal("-9999")),
                new SComplexBigDec(new BigDecimal("-9999"), new BigDecimal("-9"))
                        .add(new SComplexBigDec(new BigDecimal("1111"), new BigDecimal("-9990"))));
    }

    @Test
    void subtract() {
    }

    @Test
    void multiply() {
    }

    @Test
    void divide() {
    }

    @Test
    void negate() {
    }

    @Test
    void inverse() {
    }
}