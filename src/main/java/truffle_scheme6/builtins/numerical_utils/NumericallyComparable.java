package truffle_scheme6.builtins.numerical_utils;

import com.oracle.truffle.api.interop.UnsupportedTypeException;
import truffle_scheme6.runtime.numbers.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public interface NumericallyComparable {
    ComparisonResult compareTo(long n);

    ComparisonResult compareTo(BigInteger n);

    ComparisonResult compareTo(SFractionBigInt q);

    ComparisonResult compareTo(SFractionLong q);

    ComparisonResult compareTo(float x);

    ComparisonResult compareTo(double x);

    ComparisonResult compareTo(BigDecimal x);

    ComparisonResult compareTo(SComplexBigDec z);

    ComparisonResult compareTo(SComplexDouble z);

    ComparisonResult compareTo(SComplexFloat z);

    ComparisonResult compareTo(SComplexRational z);

    /**
     * @param o an argument that could be passed to one of the compareTo methods in this interface, but as an Object
     *          (see SNumsEqual)
     * @throws IllegalArgumentException for invalid objects, i.e. objects for which there isn't a compareTo method in this interface
     */
    default ComparisonResult compareToObj(Object o) throws IllegalArgumentException {
        return switch (o) {
            case Long l -> compareTo(l);
            case Float f -> compareTo(f);
            case Double d -> compareTo(d);
            case BigInteger bi -> compareTo(bi);
            case BigDecimal bd -> compareTo(bd);
            case SFractionBigInt fbi -> compareTo(fbi);
            case SFractionLong fbl -> compareTo(fbl);
            case SComplexBigDec cbd -> compareTo(cbd);
            case SComplexDouble cd -> compareTo(cd);
            case SComplexFloat cf -> compareTo(cf);
            case SComplexRational cr -> compareTo(cr);
            default ->
                    throw new IllegalArgumentException("Invalid value to compare numerically: " + o.getClass() + " " + o);
        };
    }

    /**
     * @param o either a {@code Long}, {@code Float}, {@code Double}, {@code BigInteger}, {@code BigDecimal}, or any other number type defined by me
     * @return a NumericallyComparable implementation that wraps the primitive, or class defined by me, or the object given
     * if it's a number defined by me (all of which implement this interface). If the object is not valid at all, no value is returned
     */
    static Optional<NumericallyComparable> implFor(Object o) {
        return Optional.ofNullable(switch (o) {
            case Long l -> new LongComparator(l);
            case Float f -> new FloatComparator(f);
            case Double d -> new DoubleComparator(d);
            case BigInteger bi -> new BigIntComparator(bi);
            case BigDecimal bd -> new BigDecComparator(bd);
            case SFraction fr -> fr;
            case SComplex co -> co;
            default -> null;
        });
    }

    /**
     * Runs {@code numbers[i - 1].compareTo(numbers[i])} for every i = 1; i < numbers.length. If numbers has less than
     * 2 elements, throws {@code IllegalArgumentException}
     *
     * @param numbers          an array of valid numbers, in accordance to {@code NumericallyComparable.isValid()}
     * @param resultComparator a predicate that takes the result of comparing each pair of {@code numbers[i - 1]} and {@code numbers[i]}
     * @return {@code true} for 1 element, otherwise the result of running the provided predicate on the result of each comparison
     * @throws IllegalArgumentException if {@code numbers} has less than 2 elements
     * @throws UnsupportedTypeException if one of the elements in {@code numbers} is an invalid number
     */
    static boolean comparePairs(Object[] numbers, Predicate<ComparisonResult> resultComparator) throws IllegalArgumentException, UnsupportedTypeException {
        if (numbers.length < 2) {
            throw new IllegalArgumentException("No enough arguments provided");
        } else {
            var result = true;

            for (int i = 1; i < numbers.length; i++) {
                var previousObj = numbers[i - 1];
                var currentObj = numbers[i];

                var oPrev = NumericallyComparable.implFor(previousObj);
                NumericallyComparable previous;
                if (oPrev.isPresent()) {
                    previous = oPrev.get();
                } else {
                    throw UnsupportedTypeException.create(numbers, "Invalid comparable number: " + previousObj);
                }

                result = result && resultComparator.test(previous.compareToObj(currentObj));
            }
            
            return result;
        }
    }
}

class LongComparator implements NumericallyComparable {
    private final long value;

    public LongComparator(long value) {
        this.value = value;
    }

    @Override
    public ComparisonResult compareTo(long n) {
        return ComparisonResult.from(Long.compare(value, n));
    }

    @Override
    public ComparisonResult compareTo(BigInteger n) {
        return ComparisonResult.from(BigInteger.valueOf(value).compareTo(n));
    }

    @Override
    public ComparisonResult compareTo(SFractionBigInt q) {
        return q.compareTo(value).invert();
    }

    @Override
    public ComparisonResult compareTo(SFractionLong q) {
        return q.compareTo(value).invert();
    }

    @Override
    public ComparisonResult compareTo(float x) {
        return ComparisonResult.from(Float.compare(value, x));
    }

    @Override
    public ComparisonResult compareTo(double x) {
        return ComparisonResult.from(Double.compare(value, x));
    }

    @Override
    public ComparisonResult compareTo(BigDecimal x) {
        return ComparisonResult.from(BigDecimal.valueOf(value).compareTo(x));
    }

    @Override
    public ComparisonResult compareTo(SComplexBigDec z) {
        return z.compareTo(value);
    }

    @Override
    public ComparisonResult compareTo(SComplexDouble z) {
        return z.compareTo(value);
    }

    @Override
    public ComparisonResult compareTo(SComplexFloat z) {
        return z.compareTo(value);
    }

    @Override
    public ComparisonResult compareTo(SComplexRational z) {
        return z.compareTo(value);
    }
}

class FloatComparator implements NumericallyComparable {
    private final float value;

    public FloatComparator(float value) {
        this.value = value;
    }

    @Override
    public ComparisonResult compareTo(long n) {
        return ComparisonResult.from(Float.compare(value, n));
    }

    @Override
    public ComparisonResult compareTo(BigInteger n) {
        return ComparisonResult.from(Float.compare(value, n.floatValue()));
    }

    @Override
    public ComparisonResult compareTo(SFractionBigInt q) {
        return q.compareTo(value).invert();
    }

    @Override
    public ComparisonResult compareTo(SFractionLong q) {
        return q.compareTo(value).invert();
    }

    @Override
    public ComparisonResult compareTo(float x) {
        return ComparisonResult.from(Float.compare(value, x));
    }

    @Override
    public ComparisonResult compareTo(double x) {
        return ComparisonResult.from(Double.compare(value, x));
    }

    @Override
    public ComparisonResult compareTo(BigDecimal x) {
        return ComparisonResult.from(BigDecimal.valueOf(value).compareTo(x));
    }

    @Override
    public ComparisonResult compareTo(SComplexBigDec z) {
        return z.compareTo(value);
    }

    @Override
    public ComparisonResult compareTo(SComplexDouble z) {
        return z.compareTo(value);
    }

    @Override
    public ComparisonResult compareTo(SComplexFloat z) {
        return z.compareTo(value);
    }

    @Override
    public ComparisonResult compareTo(SComplexRational z) {
        return z.compareTo(value);
    }
}

class DoubleComparator implements NumericallyComparable {
    private final double value;

    public DoubleComparator(double value) {
        this.value = value;
    }

    @Override
    public ComparisonResult compareTo(long n) {
        return ComparisonResult.from(Double.compare(value, n));
    }

    @Override
    public ComparisonResult compareTo(BigInteger n) {
        return ComparisonResult.from(Double.compare(value, n.doubleValue()));
    }

    @Override
    public ComparisonResult compareTo(SFractionBigInt q) {
        return q.compareTo(value).invert();
    }

    @Override
    public ComparisonResult compareTo(SFractionLong q) {
        return q.compareTo(value).invert();
    }

    @Override
    public ComparisonResult compareTo(float x) {
        return ComparisonResult.from(Double.compare(value, x));
    }

    @Override
    public ComparisonResult compareTo(double x) {
        return ComparisonResult.from(Double.compare(value, x));
    }

    @Override
    public ComparisonResult compareTo(BigDecimal x) {
        return ComparisonResult.from(BigDecimal.valueOf(value).compareTo(x));
    }

    @Override
    public ComparisonResult compareTo(SComplexBigDec z) {
        return z.compareTo(value);
    }

    @Override
    public ComparisonResult compareTo(SComplexDouble z) {
        return z.compareTo(value);
    }

    @Override
    public ComparisonResult compareTo(SComplexFloat z) {
        return z.compareTo(value);
    }

    @Override
    public ComparisonResult compareTo(SComplexRational z) {
        return z.compareTo(value);
    }
}

class BigIntComparator implements NumericallyComparable {
    private final BigInteger value;

    public BigIntComparator(BigInteger value) {
        this.value = value;
    }

    @Override
    public ComparisonResult compareTo(long n) {
        return ComparisonResult.from(value.compareTo(BigInteger.valueOf(n)));
    }

    @Override
    public ComparisonResult compareTo(BigInteger n) {
        return ComparisonResult.from(value.compareTo(n));
    }

    @Override
    public ComparisonResult compareTo(SFractionBigInt q) {
        return q.compareTo(value).invert();
    }

    @Override
    public ComparisonResult compareTo(SFractionLong q) {
        return q.compareTo(value).invert();
    }

    @Override
    public ComparisonResult compareTo(float x) {
        return ComparisonResult.from(Float.compare(value.floatValue(), x));
    }

    @Override
    public ComparisonResult compareTo(double x) {
        return ComparisonResult.from(Double.compare(value.doubleValue(), x));
    }

    @Override
    public ComparisonResult compareTo(BigDecimal x) {
        return ComparisonResult.from(new BigDecimal(value).compareTo(x));
    }

    @Override
    public ComparisonResult compareTo(SComplexBigDec z) {
        return z.compareTo(value);
    }

    @Override
    public ComparisonResult compareTo(SComplexDouble z) {
        return z.compareTo(value);
    }

    @Override
    public ComparisonResult compareTo(SComplexFloat z) {
        return z.compareTo(value);
    }

    @Override
    public ComparisonResult compareTo(SComplexRational z) {
        return z.compareTo(value);
    }
}

class BigDecComparator implements NumericallyComparable {
    private final BigDecimal value;

    public BigDecComparator(BigDecimal value) {
        this.value = value;
    }

    @Override
    public ComparisonResult compareTo(long n) {
        return ComparisonResult.from(value.compareTo(BigDecimal.valueOf(n)));
    }

    @Override
    public ComparisonResult compareTo(BigInteger n) {
        return ComparisonResult.from(value.compareTo(new BigDecimal(n)));
    }

    @Override
    public ComparisonResult compareTo(SFractionBigInt q) {
        return q.compareTo(value).invert();
    }

    @Override
    public ComparisonResult compareTo(SFractionLong q) {
        return q.compareTo(value).invert();
    }

    @Override
    public ComparisonResult compareTo(float x) {
        return ComparisonResult.from(value.compareTo(BigDecimal.valueOf(x)));
    }

    @Override
    public ComparisonResult compareTo(double x) {
        return ComparisonResult.from(value.compareTo(BigDecimal.valueOf(x)));
    }

    @Override
    public ComparisonResult compareTo(BigDecimal x) {
        return ComparisonResult.from(value.compareTo(x));
    }

    @Override
    public ComparisonResult compareTo(SComplexBigDec z) {
        return z.compareTo(value);
    }

    @Override
    public ComparisonResult compareTo(SComplexDouble z) {
        return z.compareTo(value);
    }

    @Override
    public ComparisonResult compareTo(SComplexFloat z) {
        return z.compareTo(value);
    }

    @Override
    public ComparisonResult compareTo(SComplexRational z) {
        return z.compareTo(value);
    }
}

