package truffle_scheme6.nodes.special;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import truffle_scheme6.Constants;
import truffle_scheme6.SchemeLanguage;
import truffle_scheme6.SchemeLanguageContext;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.nodes.atoms.SSymbolLiteralNode;
import truffle_scheme6.nodes.roots.SLambdaRoot;
import truffle_scheme6.runtime.SLambda;
import truffle_scheme6.utils.StaticUtils;
import truffle_scheme6.utils.StringFormatting;

public class SDefunNode extends SSpecialNode {
    @Child
    private SSymbolLiteralNode identifier;
    @Children
    private SSymbolLiteralNode[] arguments;
    @Children
    private SchemeNode[] body;

    private final FrameDescriptor frameDescriptor;
    private final String name;

    public SDefunNode(SSymbolLiteralNode identifier, SSymbolLiteralNode[] arguments, SchemeNode[] body, FrameDescriptor frameDescriptor, String name) {
        this.identifier = identifier;
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
        var context = SchemeLanguageContext.get(this);
        var lambda = new SLambda(new SLambdaRoot(sl, frameDescriptor, identifier.getSymbol().toString(), body).getCallTarget());
        context.globalScope.setVar(identifier.getSymbol(), lambda);

        return Constants.UNSPECIFIED;
    }

    @Override
    public String toString() {
        var argsStr = StringFormatting.spaced(arguments);
        argsStr = argsStr.isEmpty() ? argsStr : " " + argsStr;
        return "(define (%s%s) %s)".formatted(identifier, argsStr, StringFormatting.spaced(body));
    }
}
