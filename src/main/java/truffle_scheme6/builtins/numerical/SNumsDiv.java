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

@BuiltinInfo(name = "/", lastVarArgs = true)
@NodeChild(value = "args", type = SchemeNode.class)
public abstract class SNumsDiv extends SBuiltin {
    @Child
    protected UUnaryInverse inverter = UUnaryInverseNodeGen.create();
    @Child
    protected UBinaryDivision divider = UBinaryDivisionNodeGen.create();

    @Specialization
    public Object doPair(SList args) throws UnsupportedTypeException, ArityException {
        return switch (args) {
            case SNil _nil -> throw ArityException.create(1, -1, 0);
            case SPair pair -> {
                var car = pair.getCar();
                if (!StaticUtils.isNumber(car)) { // validating in case the list only has one element (loop wouldn't run and therefore wouldn't check)
                    throw UnsupportedTypeException.create(pair.toArray(), "Value given is not a valid number: " + car + " of type " + car.getClass() + " within " + pair);
                }

                Object result = car;
                SPair node = pair;
                if (node.getCdr() instanceof SPair) {
                    while (node.getCdr() instanceof SPair) {
                        node = (SPair) (node.getCdr());
                        car = node.getCar();
                        result = divider.execute(result, car);
                    }
                } else {
                    result = inverter.execute(result);
                }

                yield result;
            }
            default -> throw new IllegalArgumentException("Invalid args");
        };
    }
}
