package truffle_scheme6.nodes;

import com.oracle.truffle.api.dsl.ImplicitCast;
import com.oracle.truffle.api.dsl.TypeCheck;
import com.oracle.truffle.api.dsl.TypeSystem;
import truffle_scheme6.runtime.SNil;
import truffle_scheme6.runtime.numbers.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

/**
 * On the complicated structure of numbers used:
 * <p>
 * Scheme has built-in complex numbers and fractions. In complex numbers,
 * fractions can be combined with regular integers, which I represent with long and bigint.
 * That is why I have wrapper types for {@code long} and {@code BigInteger}, both of which
 * inherit from a parent class {@code SRational} that is also the parent class for fraction types.
 * They're all interchangeable when used in a complex number of rational numbers.
 * This was not necessary for float and double and BigDecimal types. There are
 * complex number classes for all 3 of those types and one could simply be upgraded to the next if necessary.
 * </p>
 * <p>
 * Maybe in the future I'll get rid of the wrapper classes and make complex numbers with fractions
 * treat whole numbers as fractions with 1 as a denominator, which might be a tradeoff worth making
 * </p>
 */
@TypeSystem({boolean.class, byte.class, long.class, float.class, double.class})
public abstract class STypes {
    @TypeCheck(SNil.class)
    public static boolean isSNull(Object value) {
        return value == SNil.SINGLETON;
    }

    /*---- REAL=>REAL CASTS ----*/

    @ImplicitCast
    public static BigInteger castBigInt(long fixnum) {
        return BigInteger.valueOf(fixnum);
    }

    @ImplicitCast
    public static SFractionLong castFractionLong(long fixnum) {
        return new SFractionLong(fixnum, 1L);
    }

    @ImplicitCast
    public static SFractionBigInt castFractionBigInt(long fixnum) {
        return new SFractionBigInt(BigInteger.valueOf(fixnum), BigInteger.ONE);
    }

    @ImplicitCast
    public static SFractionBigInt castFractionBigInt(BigInteger bigint) {
        return new SFractionBigInt(bigint, BigInteger.ONE);
    }

    @ImplicitCast
    public static SFractionBigInt castFractionBigInt(SFractionLong longFraction) {
        return new SFractionBigInt(
                BigInteger.valueOf(longFraction.numerator()),
                BigInteger.valueOf(longFraction.denominator())
        );
    }

    @ImplicitCast
    public static float castFloat(long fixnum) {
        return fixnum;
    }

    @ImplicitCast
    public static float castFloat(BigInteger bigint) {
        return bigint.floatValue();
    }

    @ImplicitCast
    public static float castFloat(SFractionLong fraction) {
        return (float) fraction.numerator() / (float) fraction.denominator();
    }

    @ImplicitCast
    public static float castFloat(SFractionBigInt fraction) {
        return fraction.numerator().floatValue() / fraction.denominator().floatValue();
    }

    @ImplicitCast
    public static double castDouble(float value) {
        return value;
    }

    @ImplicitCast
    public static double castDouble(long fixnum) {
        return fixnum;
    }

    @ImplicitCast
    public static double castDouble(BigInteger bigint) {
        return bigint.doubleValue();
    }

    @ImplicitCast
    public static double castDouble(SFractionLong fraction) {
        return (double) fraction.numerator() / (double) fraction.denominator();
    }

    @ImplicitCast
    public static double castDouble(SFractionBigInt fraction) {
        return fraction.numerator().doubleValue() / fraction.denominator().doubleValue();
    }

    @ImplicitCast
    public static BigDecimal castBigDecimal(float f) {
        return new BigDecimal(f);
    }

    @ImplicitCast
    public static BigDecimal castBigDecimal(double d) {
        return new BigDecimal(d);
    }

    @ImplicitCast
    public static BigDecimal castBigDecimal(long fixnum) {
        return BigDecimal.valueOf(fixnum);
    }

    @ImplicitCast
    public static BigDecimal castBigDecimal(BigInteger bigint) {
        return new BigDecimal(bigint);
    }

    @ImplicitCast
    public static BigDecimal castBigDecimal(SFractionLong fraction) {
        // todo revisit: what does the standard say about rational numbers that equal repeating decimals?
        //   same with a Ctrl + Shift + F of all MathContext usages that are similar to this one
        return new BigDecimal(fraction.numerator()).divide(new BigDecimal(fraction.denominator()), MathContext.DECIMAL128);
    }

    @ImplicitCast
    public static BigDecimal castBigDecimal(SFractionBigInt fraction) {
        return new BigDecimal(fraction.numerator()).divide(new BigDecimal(fraction.denominator()), MathContext.DECIMAL128);
    }

    /*---- COMPLEX=>REAL CASTS ----*/

    @ImplicitCast
    public static SComplexRational castSComplexRational(long l) {
        return new SComplexRational(
                new SFractionLong(l, 1),
                new SFractionLong(0, 1)
        );
    }

    @ImplicitCast
    public static SComplexRational castSComplexRational(BigInteger bigint) {
        return new SComplexRational(
                new SFractionBigInt(bigint, BigInteger.ONE),
                new SFractionBigInt(BigInteger.ZERO, BigInteger.ONE)
        );
    }

    @ImplicitCast
    public static SComplexRational castSComplexRational(SFractionLong fraction) {
        return new SComplexRational(
                fraction,
                new SFractionLong(0, 1)
        );
    }

    @ImplicitCast
    public static SComplexRational castSComplexRational(SFractionBigInt fraction) {
        return new SComplexRational(
                fraction,
                new SFractionBigInt(BigInteger.ZERO, BigInteger.ONE)
        );
    }

    @ImplicitCast
    public static SComplexFloat castSComplexFloat(long l) {
        return new SComplexFloat(l, 0.0f);
    }

    @ImplicitCast
    public static SComplexFloat castSComplexFloat(BigInteger bigint) {
        return new SComplexFloat(bigint.floatValue(), 0.0f);
    }

    @ImplicitCast
    public static SComplexFloat castSComplexFloat(SFractionLong fraction) {
        return new SComplexFloat(fraction.floatValue(), 0.0f);
    }

    @ImplicitCast
    public static SComplexFloat castSComplexFloat(SFractionBigInt fraction) {
        return new SComplexFloat(fraction.floatValue(), 0.0f);
    }

    @ImplicitCast
    public static SComplexFloat castSComplexFloat(float f) {
        return new SComplexFloat(f, 0.0f);
    }

    @ImplicitCast
    public static SComplexDouble castSComplexDouble(long l) {
        return new SComplexDouble(l, 0.0d);
    }

    @ImplicitCast
    public static SComplexDouble castSComplexDouble(BigInteger bigint) {
        return new SComplexDouble(bigint.doubleValue(), 0.0d);
    }

    @ImplicitCast
    public static SComplexDouble castSComplexDouble(SFractionLong fraction) {
        return new SComplexDouble(fraction.doubleValue(), 0.0d);
    }

    @ImplicitCast
    public static SComplexDouble castSComplexDouble(SFractionBigInt fraction) {
        return new SComplexDouble(fraction.doubleValue(), 0.0d);
    }

    @ImplicitCast
    public static SComplexDouble castSComplexDouble(float f) {
        return new SComplexDouble(f, 0.0d);
    }

    @ImplicitCast
    public static SComplexDouble castSComplexDouble(double d) {
        return new SComplexDouble(d, 0.0d);
    }

    @ImplicitCast
    public static SComplexBigDec castSComplexBigDec(long l) {
        return new SComplexBigDec(BigDecimal.valueOf(l), BigDecimal.ZERO);
    }

    @ImplicitCast
    public static SComplexBigDec castSComplexBigDec(BigInteger bigint) {
        return new SComplexBigDec(new BigDecimal(bigint), BigDecimal.ZERO);
    }

    @ImplicitCast
    public static SComplexBigDec castSComplexBigDec(SFractionLong fraction) {
        return new SComplexBigDec(fraction.bigDecimalValue(), BigDecimal.ZERO);
    }

    @ImplicitCast
    public static SComplexBigDec castSComplexBigDec(SFractionBigInt fraction) {
        return new SComplexBigDec(fraction.bigDecimalValue(), BigDecimal.ZERO);
    }

    @ImplicitCast
    public static SComplexBigDec castSComplexBigDec(float f) {
        return new SComplexBigDec(BigDecimal.valueOf(f), BigDecimal.ZERO);
    }

    @ImplicitCast
    public static SComplexBigDec castSComplexBigDec(double d) {
        return new SComplexBigDec(BigDecimal.valueOf(d), BigDecimal.ZERO);
    }

    @ImplicitCast
    public static SComplexBigDec castSComplexBigDec(BigDecimal bd) {
        return new SComplexBigDec(bd, BigDecimal.ZERO);
    }

    /*---- COMPLEX=>COMPLEX CASTS ----*/

    @ImplicitCast
    public static SComplexFloat castSComplexFloat(SComplexRational complex) {
        return new SComplexFloat(
                complex.real().floatValue(),
                complex.imag().floatValue()
        );
    }

    @ImplicitCast
    public static SComplexDouble castSComplexDouble(SComplexRational complex) {
        return new SComplexDouble(
                complex.real().doubleValue(),
                complex.imag().doubleValue()
        );
    }

    @ImplicitCast
    public static SComplexDouble castSComplexDouble(SComplexFloat complex) {
        return new SComplexDouble(
                complex.real(),
                complex.imag()
        );
    }

    @ImplicitCast
    public static SComplexBigDec castSComplexBigDec(SComplexRational complex) {
        return new SComplexBigDec(
                complex.real().bigDecimalValue(),
                complex.imag().bigDecimalValue()
        );
    }

    @ImplicitCast
    public static SComplexBigDec castSComplexBigDec(SComplexFloat complex) {
        return new SComplexBigDec(
                BigDecimal.valueOf(complex.real()),
                BigDecimal.valueOf(complex.imag())
        );
    }

    @ImplicitCast
    public static SComplexBigDec castSComplexBigDec(SComplexDouble complex) {
        return new SComplexBigDec(
                BigDecimal.valueOf(complex.real()),
                BigDecimal.valueOf(complex.imag())
        );
    }
}
