package truffle_scheme6.builtins.private_utils;

public enum ComparisonResult {
    LessThan,
    Equal,
    GreaterThan,
    /**
     * Intended as a value to represent inequality without implying anything else about the comparison's result
     * E.g. with complex numbers that can be compared for equality but not for being lesser or greater than other numbers
     */
    Unequal;
    
    public static ComparisonResult from(int compareToResult) {
        if (compareToResult == 0) {
            return Equal;
        } else if (compareToResult > 0) {
            return GreaterThan;
        } else {
            return LessThan;
        }
    } 
}
