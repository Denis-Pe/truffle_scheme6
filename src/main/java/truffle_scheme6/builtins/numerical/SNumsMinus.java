package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.ArityException;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.builtins.SBuiltin;

@BuiltinInfo(name = "-", lastVarArgs = true)
@NodeChild(value = "args", type = SchemeNode.class)
public abstract class SNumsMinus extends SBuiltin {
    private UUnaryNegation negator = UUnaryNegationNodeGen.create();
    private UBinarySubtraction subtractor = UBinarySubtractionNodeGen.create();

    @Specialization
    public Object doObjectArr(Object[] args) {
        if (args.length == 0) {
            throw new RuntimeException("Not enough arguments provided");
        } else if (args.length == 1) {
            return negator.execute(args[0]);
        } else {
            Object res = args[0];

            for (int i = 1; i < args.length; i++) {
                res = subtractor.execute(res, args[i]);
            }

            return res;
        }
    }
}
