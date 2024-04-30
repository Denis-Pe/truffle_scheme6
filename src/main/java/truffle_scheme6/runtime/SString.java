package truffle_scheme6.runtime;

import com.oracle.truffle.api.strings.TruffleString;
import com.oracle.truffle.api.strings.TruffleStringBuilder;
import truffle_scheme6.Constants;

import java.util.stream.IntStream;

public class SString {
    private final TruffleString value;

    public SString(TruffleString value) {
        this.value = value;
    }

    public SString(int[] codepoints) {
        TruffleStringBuilder builder = TruffleStringBuilder.create(Constants.ENCODING);
        var appender = TruffleStringBuilder.AppendCodePointNode.create();

        for (var c : codepoints) {
            appender.execute(builder, c);
        }

        this.value = builder.toStringUncached();
    }

    public SString(IntStream codepoints) {
        this(codepoints.toArray());
    }

    public SString(String str) {
        this(str.codePoints());
    }

    public TruffleString getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "\"" + value.toJavaStringUncached() + "\"";
    }
}
