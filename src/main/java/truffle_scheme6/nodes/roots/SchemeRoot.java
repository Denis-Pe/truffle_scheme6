package truffle_scheme6.nodes.roots;

import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;
import truffle_scheme6.SchemeNode;

public class SchemeRoot extends RootNode {
    SchemeNode obj;

    public SchemeRoot(TruffleLanguage<?> language, SchemeNode obj) {
        super(language);
        this.obj = obj;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        return obj.execute(frame);
    }
}
