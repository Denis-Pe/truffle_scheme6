package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.UnsupportedTypeException;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.utils.StaticUtils;

@BuiltinInfo(name = "+", lastVarArgs = true)
@NodeChild(value = "args", type = SchemeNode.class)
public abstract class SNumsAdd extends SBuiltin {
    @Child
    private UBinaryAddition adder = UBinaryAdditionNodeGen.create();

    @Specialization
    public Object doObjectArr(Object[] args) {
        if (args.length == 0) {
            return 0;
        } else if (args.length == 1) {
            if (StaticUtils.isNumber(args[0])) {
                return args[0];
            } else {
                throw new RuntimeException(UnsupportedTypeException.create(args, "Value given is not a valid number"));
            }
        } else {
            var result = args[0];

            for (int i = 1; i < args.length; i++) {
                result = adder.execute(result, args[i]);
            }

            return result;
        }
    }
}
