package truffle_scheme6.nodes.atoms;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.strings.TruffleString;
import com.oracle.truffle.api.strings.TruffleStringBuilder;
import truffle_scheme6.Constants;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.runtime.SIdentifier;

import java.util.stream.IntStream;

public final class SIdentifierLiteralNode extends SchemeNode {
    private final TruffleString value;
    private static final TruffleString.ToJavaStringNode converter = TruffleString.ToJavaStringNode.create();

    public SIdentifierLiteralNode(int[] codepoints) {
        TruffleStringBuilder builder = TruffleStringBuilder.create(Constants.ENCODING);
        var appender = TruffleStringBuilder.AppendCodePointNode.create();

        for (var c : codepoints) {
            appender.execute(builder, c);
        }

        this.value = builder.toStringUncached();
    }

    public SIdentifierLiteralNode(IntStream codepoints) {
        this(codepoints.toArray());
    }

    public SIdentifierLiteralNode(String javaString) {
        this(javaString.codePoints());
    }

    @Override
    public Object execute(VirtualFrame frame) {
        var id = new SIdentifier(value);

        var val = this.getCurrentContext().globalScope.getVar(id);
        if (val != null) {
            return val;
        } else {
            throw new RuntimeException("Symbol " + this + " is not bound to a variable");
        }
    }

    @Override
    public SIdentifier executeFrozen(VirtualFrame frame) {
        return new SIdentifier(value);
    }

    public TruffleString getValue() {
        return value;
    }

    @Override
    public String toString() {
        return converter.execute(value);
    }

}
