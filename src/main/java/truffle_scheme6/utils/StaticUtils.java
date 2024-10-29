package truffle_scheme6.utils;

import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.nodes.MaterializedFrameUser;
import truffle_scheme6.nodes.atoms.SSymbolLiteralNode;

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
}
