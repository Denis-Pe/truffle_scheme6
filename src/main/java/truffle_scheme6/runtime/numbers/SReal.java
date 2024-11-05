package truffle_scheme6.runtime.numbers;


/**
 * I hope someone at Oracle has realized how big a mistake it was to make
 * java.lang.Number but thankfully making our interface equivalent ain't too hard.
 *
 * <p>
 * One difference with java.lang.Number is that an implementing class of SReal
 * only needs to implement doubleValue and longValue, the rest of methods having default
 * implementations that cast those values to the smaller types.
 * </p>
 * 
 * <p>
 * Also adding comparison with other real numbers
 * </p>
 */
public interface SReal extends SNumber {
    double doubleValue();

    default float floatValue() {
        return (float) doubleValue();
    }

    long longValue();

    default int intValue() {
        return (int) longValue();
    }

    default short shortValue() {
        return (short) longValue();
    }

    default byte byteValue() {
        return (byte) longValue();
    }
}
