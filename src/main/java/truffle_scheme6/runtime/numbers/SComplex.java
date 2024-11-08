package truffle_scheme6.runtime.numbers;

import truffle_scheme6.builtins.private_utils.NumericallyComparable;

public interface SComplex extends NumericallyComparable {
    boolean isRealValued();
}
