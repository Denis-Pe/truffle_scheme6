package truffle_scheme6.utils;

import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.nodes.MaterializedFrameUser;
import truffle_scheme6.runtime.numbers.*;

import java.math.BigDecimal;
import java.math.BigInteger;

public class StaticUtils {
    public static void tagClosureReaders(VirtualFrame frame, String frameName, SchemeNode[] tree) {
        // todo have a mechanism at the parse level to detect whether this is actually necessary in the root-making nodes
        for (var topForm : tree) {
            topForm.accept(n -> {
                if (n instanceof MaterializedFrameUser user
                        && user.getRootName().equals(frameName)) {
                    user.setMaterializedFrame(frame.materialize());
                }

                return true;
            });
        }
    }
    
    public static boolean isNumber(Object value) {
        return switch(value) {
            case Long l -> true;
            case BigInteger bi -> true;
            case SFractionLong fl -> true;
            case SFractionBigInt fb -> true;
            case Float f -> true;
            case Double d -> true;
            case BigDecimal bd -> true;
            case SComplexLong cl -> true;
            case SComplexBigInt cb -> true;
            case SComplexFloat cf -> true;
            case SComplexDouble cd -> true;
            case SComplexBigDec cb -> true;
            default -> false;
        };
    }
}
