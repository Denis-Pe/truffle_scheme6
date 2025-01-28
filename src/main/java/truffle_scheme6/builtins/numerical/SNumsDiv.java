package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.builtins.SBuiltin;

@BuiltinInfo(name = "/", lastVarArgs = true)
@NodeChild(value = "args", type = SchemeNode.class)
public abstract class SNumsDiv extends SBuiltin {
    @Child
    protected UUnaryInverse inverter = UUnaryInverseNodeGen.create();
    @Child
    protected UBinaryDivision divider = UBinaryDivisionNodeGen.create();

    @Specialization
    public Object doObjectArr(Object[] args) {
        if (args.length == 0) {
            throw new RuntimeException("insufficient args given");
        } else if (args.length == 1) {
            return inverter.execute(args[0]);
        } else {
            var result = args[0];

            for (int i = 1; i < args.length; i++) {
                result = divider.execute(result, args[i]);
            }

            return result;
        }
    }
}
