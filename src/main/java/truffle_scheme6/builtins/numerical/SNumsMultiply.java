package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.UnsupportedTypeException;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.runtime.SList;
import truffle_scheme6.runtime.SNil;
import truffle_scheme6.runtime.SPair;
import truffle_scheme6.utils.StaticUtils;

@BuiltinInfo(name = "*", lastVarArgs = true)
@NodeChild(value = "args", type = SchemeNode.class)
public abstract class SNumsMultiply extends SBuiltin {
    @Child
    protected UBinaryMultiplication multiplier = UBinaryMultiplicationNodeGen.create();

    @Specialization
    public Object doPair(SList args) throws UnsupportedTypeException {
        return switch (args) {
            case SNil _nil -> 1;
            case SPair pair -> {
                var car = pair.getCar();
                if (!StaticUtils.isNumber(car)) { // validating in case the list only has one element (loop wouldn't run and therefore wouldn't check)
                    throw UnsupportedTypeException.create(pair.toArray(), "Value given is not a valid number: " + car + " of type " + car.getClass() + " within " + pair);
                }

                var result = car;
                SPair node = pair;
                while (node.getCdr() instanceof SPair) {
                    node = (SPair) (node.getCdr());
                    car = node.getCar();
                    result = multiplier.execute(result, car);
                }

                yield result;
            }
            default -> throw new IllegalArgumentException("Invalid args");
        };
    }
}
