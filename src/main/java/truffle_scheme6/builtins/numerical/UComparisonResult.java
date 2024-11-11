package truffle_scheme6.builtins.numerical;

/**
 * Utility enum as a result of number comparisons.
 * Package private
 */
enum UComparisonResult {
    LessThan,
    Equal,
    GreaterThan,
    /**
     * Intended as a value to represent inequality without implying anything else about the comparison's result
     * E.g. with complex numbers that can be compared for equality but not for being lesser or greater than other numbers
     */
    Unequal;
    
    public static UComparisonResult from(int compareToResult) {
        if (compareToResult == 0) {
            return Equal;
        } else if (compareToResult > 0) {
            return GreaterThan;
        } else {
            return LessThan;
        }
    }
}
