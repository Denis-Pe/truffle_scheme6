package truffle_scheme6.runtime.numbers;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class SComplexBigIntTest {

    @Test
    void isRealValued() {
        assertTrue(new SComplexBigInt(BigInteger.valueOf(1), BigInteger.valueOf(0)).isRealValued());
        assertTrue(new SComplexBigInt(BigInteger.valueOf(0), BigInteger.valueOf(0)).isRealValued());
        assertTrue(new SComplexBigInt(BigInteger.valueOf(123), BigInteger.valueOf(0)).isRealValued());
        assertFalse(new SComplexBigInt(BigInteger.valueOf(0), BigInteger.valueOf(123)).isRealValued());
        assertFalse(new SComplexBigInt(BigInteger.valueOf(123), BigInteger.valueOf(123)).isRealValued());
        assertTrue(new SComplexBigInt(new BigInteger("999".repeat(999)), BigInteger.valueOf(0)).isRealValued());
    }

    @Test
    void add() {
        assertEquals(new SComplexBigInt(BigInteger.valueOf(1), BigInteger.valueOf(1)),
                new SComplexBigInt(BigInteger.valueOf(1), BigInteger.ZERO)
                        .add(new SComplexBigInt(BigInteger.ZERO, BigInteger.valueOf(1))));

        // 7/12+11/36i + 5/18+13/24i
        assertEquals(new SComplexBigInt(new SFractionBigInt(BigInteger.valueOf(186), BigInteger.valueOf(216)), new SFractionBigInt(BigInteger.valueOf(732), BigInteger.valueOf(864))),
                new SComplexBigInt(new SFractionBigInt(BigInteger.valueOf(7), BigInteger.valueOf(12)), new SFractionBigInt(BigInteger.valueOf(11), BigInteger.valueOf(36)))
                        .add(new SComplexBigInt(new SFractionBigInt(BigInteger.valueOf(5), BigInteger.valueOf(18)), new SFractionBigInt(BigInteger.valueOf(13), BigInteger.valueOf(24)))));
    }

    @Test
    void subtract() {
        assertEquals(new SComplexBigInt(BigInteger.valueOf(15), BigInteger.valueOf(0)),
                new SComplexBigInt(BigInteger.valueOf(30), BigInteger.valueOf(9999))
                        .subtract(new SComplexBigInt(BigInteger.valueOf(15), BigInteger.valueOf(9999))));

        assertEquals(new SComplexBigInt(BigInteger.valueOf(45), BigInteger.valueOf(10)),
                new SComplexBigInt(BigInteger.valueOf(30), BigInteger.valueOf(5))
                        .subtract(new SComplexBigInt(BigInteger.valueOf(-15), BigInteger.valueOf(-5))));
    }

    @Test
    void multiply() {
        assertEquals(new SComplexBigInt(BigInteger.valueOf(0), BigInteger.valueOf(20)),
                new SComplexBigInt(BigInteger.valueOf(2), BigInteger.valueOf(4))
                        .multiply(new SComplexBigInt(BigInteger.valueOf(4), BigInteger.valueOf(2))));

        assertEquals(new SComplexBigInt(BigInteger.valueOf(27), BigInteger.valueOf(36)),
                new SComplexBigInt(BigInteger.valueOf(-3), BigInteger.valueOf(6))
                        .multiply(new SComplexBigInt(BigInteger.valueOf(3), BigInteger.valueOf(-6))));
    }

    @Test
    void divide() {
        assertEquals(new SComplexBigInt(new SFractionBigInt(BigInteger.valueOf(4), BigInteger.valueOf(5)), new SFractionBigInt(BigInteger.valueOf(-3), BigInteger.valueOf(5))),
                new SComplexBigInt(BigInteger.valueOf(2), BigInteger.valueOf(1))
                        .divide(new SComplexBigInt(BigInteger.valueOf(1), BigInteger.valueOf(2))));
        
        assertEquals(new SComplexBigInt(BigInteger.ONE.negate(), BigInteger.ZERO),
                new SComplexBigInt(BigInteger.valueOf(6), BigInteger.valueOf(-3))
                        .divide(new SComplexBigInt(BigInteger.valueOf(-6), BigInteger.valueOf(3))));
    }

    @Test
    void negate() {
        assertEquals(new SComplexBigInt(BigInteger.valueOf(1), BigInteger.valueOf(1)),
                new SComplexBigInt(BigInteger.valueOf(-1), BigInteger.valueOf(-1)).negate());

        assertEquals(new SComplexBigInt(BigInteger.valueOf(-99), BigInteger.ZERO),
                new SComplexBigInt(BigInteger.valueOf(99), BigInteger.ZERO).negate());
    }

    @Test
    void inverse() {
        assertEquals(new SComplexBigInt(new SFractionBigInt(BigInteger.valueOf(1), BigInteger.valueOf(10)), new SFractionBigInt(BigInteger.valueOf(-1), BigInteger.valueOf(5))),
                new SComplexBigInt(BigInteger.valueOf(2), BigInteger.valueOf(4)).inverse());
        
        assertEquals(new SComplexBigInt(new SFractionBigInt(BigInteger.valueOf(1), BigInteger.valueOf(15)), new SFractionBigInt(BigInteger.valueOf(2), BigInteger.valueOf(15))),
                new SComplexBigInt(BigInteger.valueOf(3), BigInteger.valueOf(-6)).inverse());
    }
}