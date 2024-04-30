package truffle_scheme6.runtime;

import com.oracle.truffle.api.strings.TruffleString;
import com.oracle.truffle.api.strings.TruffleStringBuilder;
import truffle_scheme6.Constants;

import java.util.stream.IntStream;

// todo more efficient, cached implementation
public class SIdentifier {
    private final TruffleString value;
    private static final TruffleString.ToJavaStringNode converter = TruffleString.ToJavaStringNode.create();

    public SIdentifier(int[] codepoints) {
        TruffleStringBuilder builder = TruffleStringBuilder.create(Constants.ENCODING);
        var appender = TruffleStringBuilder.AppendCodePointNode.create();

        for (var c : codepoints) {
            appender.execute(builder, c);
        }

        this.value = builder.toStringUncached();
    }

    public SIdentifier(IntStream codepoints) {
        this(codepoints.toArray());
    }

    public SIdentifier(String str) {
        this(str.codePoints());
    }

    public SIdentifier(TruffleString value) {
        this.value = value;
    }

    public TruffleString getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toJavaStringUncached();
    }
}
