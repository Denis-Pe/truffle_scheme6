package truffle_scheme6.runtime;

import com.oracle.truffle.api.strings.TruffleString;
import com.oracle.truffle.api.strings.TruffleStringBuilder;
import truffle_scheme6.Constants;

public class SChar {
    private final TruffleString value;
    private static final TruffleString.ToJavaStringNode converter = TruffleString.ToJavaStringNode.create();

    public SChar(int codepoint) {
        TruffleStringBuilder builder = TruffleStringBuilder.create(Constants.ENCODING);
        var appender = TruffleStringBuilder.AppendCodePointNode.create();

        appender.execute(builder, codepoint);


        this.value = builder.toStringUncached();
    }

    public SChar(char c) {
        this((int) c);
    }

    public SChar(TruffleString value) {
        this.value = value;
    }

    public TruffleString getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "#\\" + value.toJavaStringUncached();
    }

}
