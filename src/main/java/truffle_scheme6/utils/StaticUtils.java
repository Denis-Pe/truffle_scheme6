package truffle_scheme6.utils;

import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.nodes.atoms.SSymbolLiteralNode;

import java.util.Arrays;

public class StaticUtils {
    public static void tagClosureReaders(VirtualFrame frame, String frameName, SchemeNode[] tree) {
        // todo have a mechanism at the parse level to detect whether this is actually necessary in the root-making nodes
        Arrays.stream(tree).forEach(topForm -> {
            topForm.accept(n -> {
                if (n instanceof SSymbolLiteralNode sym
                        && sym.getVarDispatch() instanceof SSymbolLiteralNode.ReadFromMaterialized readMaterialized
                        && readMaterialized.getRootName().equals(frameName)) {
                    readMaterialized.setMatFrame(frame.materialize());
                }

                return true;
            });
        });
    }
}
