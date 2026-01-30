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

@BuiltinInfo(name = ">", lastVarArgs = true)
@NodeChild(value = "args", type = SchemeNode.class)
public abstract class SNumsDecreasing extends SBuiltin {
    @Child
    protected URealComparator comparator = URealComparatorNodeGen.create();

    @Specialization
    public Object doPair(SList args) {
        return switch (args) {
            case SNil _nil -> throw new RuntimeException(ArityException.create(1, -1, 0));
            case SPair pair -> {
                var car = pair.getCar();
                if (!StaticUtils.isNumber(car)) { // validating in case the list only has one element (loop wouldn't run and therefore wouldn't check)
                    throw new RuntimeException(UnsupportedTypeException.create(pair.toArray(), "Value given is not a valid number: " + car + " of type " + car.getClass() + " within " + pair));
                }

                var isDecreasing = true;
                SPair node = pair;
                while (node.getCdr() instanceof SPair) {
                    node = (SPair) (node.getCdr());
                    isDecreasing = isDecreasing && comparator.execute(car, node.getCar()) == UComparisonResult.GreaterThan;
                    car = node.getCar();
                }

                yield isDecreasing;
            }
            default -> throw new IllegalArgumentException("Invalid args");
        };
    }
}
