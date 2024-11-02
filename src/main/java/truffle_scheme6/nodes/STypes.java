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
@TypeSystem({boolean.class, byte.class, float.class, double.class})
public abstract class STypes {
    @TypeCheck(SNil.class)
    public static boolean isSNull(Object value) {
        return value == SNil.SINGLETON;
    }

    @ImplicitCast
    public static SBigInt castBigInt(SFixnum fixnum) {
        return new SBigInt(BigInteger.valueOf(fixnum.getValue()));
    }

    @ImplicitCast
    public static SFractionLong castFractionLong(SFixnum fixnum) {
        return new SFractionLong(fixnum.getValue(), 1);
    }

    @ImplicitCast
    public static SFractionBigInt castFractionBigInt(SFixnum fixnum) {
        return new SFractionBigInt(BigInteger.valueOf(fixnum.getValue()), BigInteger.ONE);
    }

    @ImplicitCast
    public static SFractionBigInt castFractionBigInt(SBigInt bigint) {
        return new SFractionBigInt(bigint.getValue(), BigInteger.ONE);
    }

    @ImplicitCast
    public static float castFloat(SFixnum fixnum) {
        return (float) fixnum.getValue();
    }

    @ImplicitCast
    public static float castFloat(SBigInt bigint) {
        return bigint.getValue().floatValue();
    }

    @ImplicitCast
    public static float castFloat(SFractionLong fraction) {
        return (float) fraction.getNumerator() / (float) fraction.getDenominator();
    }

    @ImplicitCast
    public static float castFloat(SFractionBigInt fraction) {
        return fraction.getNumerator().floatValue() / fraction.getDenominator().floatValue();
    }

    @ImplicitCast
    public static double castDouble(float value) {
        return value;
    }

    @ImplicitCast
    public static double castDouble(SFixnum fixnum) {
        return (double) fixnum.getValue();
    }

    @ImplicitCast
    public static double castDouble(SBigInt bigint) {
        return bigint.getValue().doubleValue();
    }

    @ImplicitCast
    public static double castDouble(SFractionLong fraction) {
        return (double) fraction.getNumerator() / (double) fraction.getDenominator();
    }

    @ImplicitCast
    public static double castDouble(SFractionBigInt fraction) {
        return fraction.getNumerator().doubleValue() / fraction.getDenominator().doubleValue();
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
    public static BigDecimal castBigDecimal(SFixnum fixnum) {
        return new BigDecimal(fixnum.getValue());
    }

    @ImplicitCast
    public static BigDecimal castBigDecimal(SBigInt bigint) {
        return new BigDecimal(bigint.getValue());
    }

    @ImplicitCast
    public static BigDecimal castBigDecimal(SFractionLong fraction) {
        // todo revisit: what does the standard say about rational numbers that equal repeating decimals?
        //   same with a Ctrl + Shift + F of all MathContext usages that are similar to this one
        return new BigDecimal(fraction.getNumerator()).divide(new BigDecimal(fraction.getDenominator()), MathContext.DECIMAL128);
    }

    @ImplicitCast
    public static BigDecimal castBigDecimal(SFractionBigInt fraction) {
        return new BigDecimal(fraction.getNumerator()).divide(new BigDecimal(fraction.getDenominator()), MathContext.DECIMAL128);
    }
}
