package truffle_scheme6.nodes.atoms;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.strings.TruffleString;
import com.oracle.truffle.api.strings.TruffleStringBuilder;
import truffle_scheme6.Constants;
import truffle_scheme6.SchemeNode;
import truffle_scheme6.runtime.SChar;

public class SCharacterLiteralNode extends SchemeNode {
    // todo one character string for now. Will look into turning this into a byte[] later,
    //  for now I don't want to spend much time on it
    private final TruffleString value;
    private static final TruffleString.ToJavaStringNode converter = TruffleString.ToJavaStringNode.create();


    public SCharacterLiteralNode(char c) {
        this((int) c);
    }

    public SCharacterLiteralNode(int unsignedCodepointInt) {
        var builder = TruffleStringBuilder.create(Constants.ENCODING);
        var appender = TruffleStringBuilder.AppendCodePointNode.create();

        appender.execute(builder, unsignedCodepointInt);

        this.value = builder.toStringUncached();
    }


    @Override
    public Object execute(VirtualFrame frame) {
        return new SChar(value);
    }

    @Override
    public Object freeze(VirtualFrame frame) {
        return new SChar(value);
    }

    public TruffleString getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "#\\" + converter.execute(value);
    }

}
