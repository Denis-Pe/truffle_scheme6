package truffle_scheme6.builtins.numerical;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.UnsupportedTypeException;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import truffle_scheme6.annotations.BuiltinInfo;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.builtins.SBuiltin;
import truffle_scheme6.runtime.numbers.*;

import java.math.BigDecimal;
import java.math.BigInteger;

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
            return switch(args[0]) {
                case Long l -> args[0];
                case BigInteger bi -> args[0];
                case SFractionLong fl -> args[0];
                case SFractionBigInt fb -> args[0];
                case Float f -> args[0];
                case Double d -> args[0];
                case BigDecimal bd -> args[0];
                case SComplexLong cl -> args[0];
                case SComplexBigInt cb -> args[0];
                case SComplexFloat cf -> args[0];
                case SComplexDouble cd -> args[0];
                case SComplexBigDec cb -> args[0];
                default -> throw new RuntimeException(UnsupportedTypeException.create(args, "Value given is not a valid number"));
            };
        } else {
            var result = args[0];

            for (int i = 1; i < args.length; i++) {
                result = adder.execute(result, args[i]);
            }

            return result;
        }
    }
}
