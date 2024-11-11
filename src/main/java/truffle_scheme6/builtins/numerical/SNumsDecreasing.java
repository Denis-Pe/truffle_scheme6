package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.nodes.functions.SReadArgSlotNode;
import truffle_scheme6.nodes.functions.SReadVarArgsNode;

@BuiltinInfo(name = ">", lastVarArgs = true)
@NodeChild(value = "arg", type = SReadVarArgsNode.class)
public abstract class SNumsDecreasing extends SBuiltin {
    protected UNumberComparator comparator = UNumberComparatorNodeGen.create();

    @Specialization
    public Object doObjectArr(Object[] args) {
        if (args.length == 0) {
            throw new RuntimeException("Not enough arguments given");
        } else if (args.length == 1) {
            return true;
        } else {
            var equal = true;
            for (int i = 1; i < args.length; i++) {
                equal = equal && comparator.execute(args[i - 1], args[i]) == UComparisonResult.GreaterThan;
            }
            return equal;
        }
    }
}
