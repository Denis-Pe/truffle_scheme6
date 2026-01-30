package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.ArityException;
import com.oracle.truffle.api.interop.UnsupportedTypeException;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.runtime.SList;
import truffle_scheme6.runtime.SNil;
import truffle_scheme6.runtime.SPair;
import truffle_scheme6.utils.StaticUtils;

@BuiltinInfo(name = "<=", lastVarArgs = true)
@NodeChild(value = "args", type = SchemeNode.class)
public abstract class SNumsNonDecreasing extends SBuiltin {
    @Child
    protected URealComparator comparator = URealComparatorNodeGen.create();

    @Specialization
    public Object doPair(SList args) throws UnsupportedTypeException, ArityException {
        return switch (args) {
            case SNil _nil -> throw ArityException.create(1, -1, 0);
            case SPair pair -> {
                var car = pair.getCar();
                if (!StaticUtils.isNumber(car)) { // validating in case the list only has one element (loop wouldn't run and therefore wouldn't check)
                    throw UnsupportedTypeException.create(pair.toArray(), "Value given is not a valid number: " + car + " of type " + car.getClass() + " within " + pair);
                }

                var isntDecreasing = true;
                SPair node = pair;
                while (node.getCdr() instanceof SPair) {
                    node = (SPair) (node.getCdr());
                    var comparison = comparator.execute(car, node.getCar());
                    isntDecreasing = isntDecreasing && (comparison == UComparisonResult.Equal || comparison == UComparisonResult.LessThan);
                    car = node.getCar();
                }

                yield isntDecreasing;
            }
            default -> throw new IllegalArgumentException("Invalid args");
        };
    }
}
