package p.denis.roots;

import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;
import p.denis.SchemeObject;

public class SchemeRoot extends RootNode {
    SchemeObject obj;

    public SchemeRoot(TruffleLanguage<?> language, SchemeObject obj) {
        super(language);
        this.obj = obj;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        return obj.execute(frame);
    }
}
