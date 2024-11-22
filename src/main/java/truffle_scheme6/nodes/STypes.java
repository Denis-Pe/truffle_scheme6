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
    public static boolean isNull(Object value) {
        return value == SNil.SINGLETON;
    }

    /*---- REAL=>REAL CASTS ----*/

    @ImplicitCast
    public static SBigInt castBigInt(long fixnum) {
        return new SBigInt(fixnum);
    }

    @ImplicitCast
    public static SFractionLong castFractionLong(long fixnum) {
        return new SFractionLong(fixnum);
    }

    @ImplicitCast
    public static SFractionBigInt castFractionBigInt(long fixnum) {
        return new SFractionBigInt(BigInteger.valueOf(fixnum));
    }

    @ImplicitCast
    public static SFractionBigInt castFractionBigInt(SBigInt bigint) {
        return new SFractionBigInt(bigint.value());
    }

    @ImplicitCast
    public static SFractionBigInt castFractionBigInt(SFractionLong longFraction) {
        return new SFractionBigInt(
                BigInteger.valueOf(longFraction.numerator()),
                BigInteger.valueOf(longFraction.denominator())
        );
    }

    @ImplicitCast
    public static SBigDec castBigDe(long fixnum) {
        return new SBigDec(fixnum);
    }

    @ImplicitCast
    public static SBigDec castBigDec(SBigInt bigint) {
        return new SBigDec(bigint);
    }

    @ImplicitCast
    public static SBigDec castBigDec(SFractionLong fraction) {
        // todo revisit: what does the standard say about rational numbers that equal repeating decimals?
        //   same with a Ctrl + Shift + F of all MathContext usages that are similar to this one
        return new SBigDec(new BigDecimal(fraction.numerator()).divide(new BigDecimal(fraction.denominator()), MathContext.DECIMAL128));
    }

    @ImplicitCast
    public static SBigDec castBigDecimal(SFractionBigInt fraction) {
        return new SBigDec(new BigDecimal(fraction.numerator()).divide(new BigDecimal(fraction.denominator()), MathContext.DECIMAL128));
    }

    @ImplicitCast
    public static float castFloat(long fixnum) {
        return fixnum;
    }

    @ImplicitCast
    public static float castFloat(SBigInt bigint) {
        return bigint.value().floatValue();
    }

    @ImplicitCast
    public static float castFloat(SBigDec bigdec) {
        return bigdec.value().floatValue();
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
    public static double castDouble(long fixnum) {
        return fixnum;
    }

    @ImplicitCast
    public static double castDouble(SBigInt bigint) {
        return bigint.value().doubleValue();
    }

    @ImplicitCast
    public static double castDouble(SBigDec bigdec) {
        return bigdec.value().doubleValue();
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
    public static double castDouble(float value) {
        return value;
    }

    /*---- REAL=>COMPLEX CASTS ----*/

    @ImplicitCast
    public static SComplexLong castSComplexLong(long l) {
        return new SComplexLong(
                new SFractionLong(l),
                new SFractionLong(0)
        );
    }

    @ImplicitCast
    public static SComplexLong castSComplexLong(SFractionLong fraction) {
        return new SComplexLong(
                fraction,
                new SFractionLong(0)
        );
    }

    @ImplicitCast
    public static SComplexBigInt castSComplexBigInt(long l) {
        return new SComplexBigInt(
                new SFractionBigInt(BigInteger.valueOf(l)),
                new SFractionBigInt(BigInteger.ZERO)
        );
    }

    @ImplicitCast
    public static SComplexBigInt castSComplexBigInt(SBigInt bigint) {
        return new SComplexBigInt(
                new SFractionBigInt(bigint.value()),
                new SFractionBigInt(BigInteger.ZERO)
        );
    }

    @ImplicitCast
    public static SComplexBigInt castSComplexBigInt(SFractionLong fraction) {
        return new SComplexBigInt(
                fraction.asBigInt(),
                new SFractionBigInt(BigInteger.ZERO)
        );
    }

    @ImplicitCast
    public static SComplexBigInt castSComplexBigInt(SFractionBigInt fraction) {
        return new SComplexBigInt(
                fraction,
                new SFractionBigInt(BigInteger.ZERO)
        );
    }

    @ImplicitCast
    public static SComplexBigDec castSComplexBigDec(long l) {
        return new SComplexBigDec(BigDecimal.valueOf(l), BigDecimal.ZERO);
    }

    @ImplicitCast
    public static SComplexBigDec castSComplexBigDec(SBigInt bigint) {
        return new SComplexBigDec(new BigDecimal(bigint.value()), BigDecimal.ZERO);
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
    public static SComplexBigDec castSComplexBigDec(SBigDec bd) {
        return new SComplexBigDec(bd.value(), BigDecimal.ZERO);
    }

    @ImplicitCast
    public static SComplexFloat castSComplexFloat(long l) {
        return new SComplexFloat(l, 0.0f);
    }

    @ImplicitCast
    public static SComplexFloat castSComplexFloat(SBigInt bigint) {
        return new SComplexFloat(bigint.value().floatValue(), 0.0f);
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
    public static SComplexDouble castSComplexDouble(SBigInt bigint) {
        return new SComplexDouble(bigint.value().doubleValue(), 0.0d);
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

    /*---- COMPLEX=>COMPLEX CASTS ----*/

    @ImplicitCast
    public static SComplexBigInt castSComplexBigInt(SComplexLong complex) {
        return new SComplexBigInt(
                complex.real().asBigInt(),
                complex.imag().asBigInt()
        );
    }

    @ImplicitCast
    public static SComplexBigDec castSComplexBigDec(SComplexLong complex) {
        return new SComplexBigDec(
                complex.real().bigDecimalValue(),
                complex.imag().bigDecimalValue()
        );
    }

    @ImplicitCast
    public static SComplexBigDec castSComplexBigDec(SComplexBigInt complex) {
        return new SComplexBigDec(
                complex.real().bigDecimalValue(),
                complex.imag().bigDecimalValue()
        );
    }

    @ImplicitCast
    public static SComplexFloat castSComplexFloat(SComplexLong complex) {
        return new SComplexFloat(
                complex.real().floatValue(),
                complex.imag().floatValue()
        );
    }

    @ImplicitCast
    public static SComplexFloat castSComplexFloat(SComplexBigInt complex) {
        return new SComplexFloat(
                complex.real().floatValue(),
                complex.imag().floatValue()
        );
    }

    @ImplicitCast
    public static SComplexFloat castSComplexFloat(SComplexBigDec complex) {
        return new SComplexFloat(
                complex.real().floatValue(),
                complex.imag().floatValue()
        );
    }

    @ImplicitCast
    public static SComplexDouble castSComplexDouble(SComplexLong complex) {
        return new SComplexDouble(
                complex.real().doubleValue(),
                complex.imag().doubleValue()
        );
    }

    @ImplicitCast
    public static SComplexDouble castSComplexDouble(SComplexBigInt complex) {
        return new SComplexDouble(
                complex.real().doubleValue(),
                complex.imag().doubleValue()
        );
    }

    @ImplicitCast
    public static SComplexDouble castSComplexDouble(SComplexBigDec complex) {
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
}
