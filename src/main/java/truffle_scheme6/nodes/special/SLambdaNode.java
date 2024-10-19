package truffle_scheme6.nodes.special;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.SchemeLanguage;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.nodes.atoms.SSymbolLiteralNode;
import truffle_scheme6.nodes.roots.SLambdaRoot;
import truffle_scheme6.runtime.SLambda;

public class SLambdaNode extends SSpecialNode {
    @Children
    private SSymbolLiteralNode[] arguments;
    @Children
    private SchemeNode[] body;

    private final FrameDescriptor frameDescriptor;

    public SLambdaNode(SSymbolLiteralNode[] arguments, SchemeNode[] body, FrameDescriptor frameDescriptor) {
        this.arguments = arguments;
        this.body = body;
        this.frameDescriptor = frameDescriptor;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        SchemeLanguage sl = SchemeLanguage.get(this);
        return new SLambda(new SLambdaRoot(sl, frameDescriptor, body).getCallTarget());
    }
}
