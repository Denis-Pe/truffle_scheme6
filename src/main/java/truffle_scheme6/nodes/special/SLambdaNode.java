package truffle_scheme6.nodes.special;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.SchemeLanguage;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.nodes.atoms.SSymbolLiteralNode;
import truffle_scheme6.nodes.roots.SLambdaRoot;
import truffle_scheme6.runtime.SLambda;
import truffle_scheme6.utils.StaticUtils;
import truffle_scheme6.utils.StringFormatting;

public class SLambdaNode extends SSpecialNode {
    @Children
    private SSymbolLiteralNode[] arguments;
    @Children
    private SchemeNode[] body;

    private final FrameDescriptor frameDescriptor;

    private final String name;

    public SLambdaNode(SSymbolLiteralNode[] arguments, SchemeNode[] body, FrameDescriptor frameDescriptor, String name) {
        this.arguments = arguments;
        this.body = body;
        this.frameDescriptor = frameDescriptor;
        this.name = name;

        if (body.length > 0) body[body.length - 1].setIsTail();
    }

    @Override
    public Object execute(VirtualFrame frame) {
        StaticUtils.tagClosureReaders(frame, name, body);

        var sl = SchemeLanguage.get(this);
        return new SLambda(new SLambdaRoot(sl, frameDescriptor, body).getCallTarget());
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "(lambda "
                + "(" + StringFormatting.spaced(arguments) + ") "
                + StringFormatting.spaced(body)
                + ")";
    }
}
