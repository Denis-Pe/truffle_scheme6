package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.UnsupportedTypeException;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.builtins.numerical_utils.ComparisonResult;
import truffle_scheme6.builtins.numerical_utils.NumericallyComparable;
import truffle_scheme6.nodes.functions.SReadVarArgsNode;

@BuiltinInfo(name = "<", lastVarArgs = true)
@NodeChild(value = "arg", type = SReadVarArgsNode.class)
public abstract class SNumsIncreasing extends SBuiltin {
    @Specialization
    public Object doObjectArr(Object[] args) {
        if (args.length == 0) {
            throw new RuntimeException("Not enough arguments given");
        } else if (args.length == 1) {
            return true;
        } else {
            try {
                return NumericallyComparable.comparePairs(args, comparisonResult -> comparisonResult == ComparisonResult.LessThan);
            } catch (UnsupportedTypeException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
