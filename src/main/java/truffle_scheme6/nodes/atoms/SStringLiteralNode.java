package truffle_scheme6.nodes.atoms;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.strings.TruffleString;
import com.oracle.truffle.api.strings.TruffleStringBuilder;
import truffle_scheme6.Constants;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.runtime.SString;

import java.util.stream.IntStream;

public final class SStringLiteralNode extends SchemeNode {
    private final TruffleString value;
    private static final TruffleString.ToJavaStringNode converter = TruffleString.ToJavaStringNode.create();

    public SStringLiteralNode(int[] codepoints) {
        TruffleStringBuilder builder = TruffleStringBuilder.create(Constants.ENCODING);
        var appender = TruffleStringBuilder.AppendCodePointNode.create();

        for (var c : codepoints) {
            appender.execute(builder, c);
        }

        this.value = builder.toStringUncached();
    }

    public SStringLiteralNode(IntStream codepoints) {
        this(codepoints.toArray());
    }

    public SStringLiteralNode(String str) {
        this(str.codePoints());
    }

    @Override
    public Object execute(VirtualFrame frame) {
        return new SString(value);
    }

    @Override
    public Object freeze(VirtualFrame frame) {
        return new SString(value);
    }

    @Override
    public String toString() {
        return "\"" + converter.execute(value) + "\"";
    }

}
