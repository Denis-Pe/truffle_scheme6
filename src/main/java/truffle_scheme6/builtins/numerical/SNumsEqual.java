package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.UnsupportedTypeException;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.builtins.numerical_utils.ComparisonResult;
import truffle_scheme6.builtins.numerical_utils.NumericallyComparable;
import truffle_scheme6.nodes.functions.SReadVarArgsNode;

@BuiltinInfo(name = "=", lastVarArgs = true)
@NodeChild(value = "args", type = SReadVarArgsNode.class)
public abstract class SNumsEqual extends SBuiltin {
    @Specialization
    public boolean doObjectArr(Object[] args) {
        if (args.length == 0) {
            throw new RuntimeException("Not enough arguments given");
        } else {
            var equal = true;
            
            var first = args[0];
            var ocFirst = NumericallyComparable.implFor(first);
            NumericallyComparable cFirst = null;
            if (ocFirst.isPresent()) {
                cFirst = ocFirst.get();
            } else {
                throw new RuntimeException(UnsupportedTypeException.create(args, "Value given is not a comparable number: " + first));
            }

            for (int i = 1; i < args.length; i++) {
                var current = args[i];

                try {
                    equal = equal && cFirst.compareToObj(current) == ComparisonResult.Equal;
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException(UnsupportedTypeException.create(args, "Value given is not a comparable number: " + current));
                }
            }

            return equal;
        }
    }
}
